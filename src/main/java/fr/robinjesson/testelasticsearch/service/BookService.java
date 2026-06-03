package fr.robinjesson.testelasticsearch.service;

import fr.robinjesson.testelasticsearch.model.opensearch.BookDocument;
import fr.robinjesson.testelasticsearch.model.postgres.BookEntity;
import fr.robinjesson.testelasticsearch.repo.BookPostgresRepository;
import fr.robinjesson.testelasticsearch.repo.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookPostgresRepository bookPostgresRepository; // SQL
    private final BookRepository bookRepository; // OpenSearch

    @Transactional
    public BookEntity saveBook(BookEntity bookEntity) {
        BookEntity savedEntity = bookPostgresRepository.save(bookEntity);

        BookDocument document = new BookDocument();
        document.setId(savedEntity.getId());
        document.setTitle(savedEntity.getTitle());
        document.setContent(savedEntity.getContent());
        document.setAuthor(savedEntity.getAuthor());

        bookRepository.save(document);

        return savedEntity;
    }
    public List<BookDocument> searchBooks(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }

        // Si l'utilisateur tape "Hi Pot", on le transforme en "Hi* Pot*"
        // Cela indique à OpenSearch de chercher les mots commençant par Hi OU commençant par Pot
        String formattedQuery = query.trim().replaceAll("\\s+", "* ") + "*";

        return bookRepository.findByTitleContainingOrContentContaining(formattedQuery, formattedQuery);
    }

    @Transactional
    public void deleteBook(final Long id) {
        bookPostgresRepository.deleteById(id);
        bookRepository.deleteById(id);
    }
}
