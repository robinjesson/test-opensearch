package fr.robinjesson.testelasticsearch.service;

import fr.robinjesson.testelasticsearch.event.BookEvent;
import fr.robinjesson.testelasticsearch.model.opensearch.BookDocument;
import fr.robinjesson.testelasticsearch.model.postgres.BookEntity;
import fr.robinjesson.testelasticsearch.model.postgres.CategoryEntity;
import fr.robinjesson.testelasticsearch.repo.opensearch.BookOpensearchRepository;
import fr.robinjesson.testelasticsearch.repo.postgres.BookPostgresRepository;
import fr.robinjesson.testelasticsearch.repo.postgres.CategoryPostgresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookPostgresRepository bookPostgresRepository;
    private final CategoryPostgresRepository categoryPostgresRepository;
    private final BookOpensearchRepository bookOpensearchRepository; // Gardé pour la méthode searchBooks
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public BookEntity saveBook(BookEntity bookEntity) {
        if (bookEntity.getCategories() != null && !bookEntity.getCategories().isEmpty()) {
            Set<CategoryEntity> managedCategories = bookEntity.getCategories().stream()
                    .map(cat -> categoryPostgresRepository.findById(cat.getId())
                            .orElseThrow(() -> new IllegalArgumentException("La catégorie avec l'ID " + cat.getId() + " n'existe pas !")))
                    .collect(Collectors.toSet());
            bookEntity.setCategories(managedCategories);
        }

        BookEntity savedEntity = bookPostgresRepository.save(bookEntity);

        eventPublisher.publishEvent(new BookEvent(savedEntity.getId(), BookEvent.ActionType.SAVE));

        return savedEntity;
    }

    public Page<BookDocument> searchBooksWithOpensearch(String query, int page, int size) {
        if (query == null || query.isBlank()) {
            return Page.empty();
        }

        String formattedQuery = query.trim().replaceAll("\\s+", "* ") + "*";

        Pageable pageable = PageRequest.of(page, size);

        return bookOpensearchRepository.findByTitleContainingOrCategoriesContaining(formattedQuery, pageable);
    }

    public Page<BookEntity> searchBooksWithHibernate(String query, int page, int size) {
        if (query == null || query.isBlank()) {
            return Page.empty();
        }
        Pageable pageable = PageRequest.of(page, size);
        return bookPostgresRepository.findByTitleContainingOrContentContainingIgnoreCase(query, pageable);
    }

    @Transactional
    public void deleteBook(final Long id) {
        bookPostgresRepository.deleteById(id);
        eventPublisher.publishEvent(new BookEvent(id, BookEvent.ActionType.DELETE));
    }

    @Transactional
    public BookEntity updateBook(Long id, BookEntity updatedEntity) {
        BookEntity existingBook = bookPostgresRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livre introuvable avec l'ID : " + id));

        existingBook.setTitle(updatedEntity.getTitle());
        existingBook.setContent(updatedEntity.getContent());
        existingBook.setAuthor(updatedEntity.getAuthor());

        if (updatedEntity.getCategories() != null) {
            Set<CategoryEntity> managedCategories = updatedEntity.getCategories().stream()
                    .map(cat -> categoryPostgresRepository.findById(cat.getId())
                            .orElseThrow(() -> new IllegalArgumentException("La catégorie ID " + cat.getId() + " n'existe pas !")))
                    .collect(Collectors.toSet());

            existingBook.setCategories(managedCategories);
        } else {
            existingBook.getCategories().clear();
        }

        BookEntity savedEntity = bookPostgresRepository.save(existingBook);

        eventPublisher.publishEvent(new BookEvent(savedEntity.getId(), BookEvent.ActionType.UPDATE));

        return savedEntity;
    }


}