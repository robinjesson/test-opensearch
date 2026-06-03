package fr.robinjesson.testelasticsearch.service;

import fr.robinjesson.testelasticsearch.model.opensearch.BookDocument;
import fr.robinjesson.testelasticsearch.model.postgres.BookEntity;
import fr.robinjesson.testelasticsearch.model.postgres.CategoryEntity;
import fr.robinjesson.testelasticsearch.repo.postgres.BookPostgresRepository;
import fr.robinjesson.testelasticsearch.repo.opensearch.BookRepository;
import fr.robinjesson.testelasticsearch.repo.postgres.CategoryPostgresRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookPostgresRepository bookPostgresRepository;
    private final BookRepository bookRepository;
    private final CategoryPostgresRepository categoryPostgresRepository;

    @Transactional
    public BookEntity saveBook(BookEntity bookEntity) {
        // 1. Récupérer les vraies catégories rattachées à la session Hibernate
        if (bookEntity.getCategories() != null && !bookEntity.getCategories().isEmpty()) {
            Set<CategoryEntity> managedCategories = bookEntity.getCategories().stream()
                    .map(cat -> categoryPostgresRepository.findById(cat.getId())
                            .orElseThrow(() -> new IllegalArgumentException("La catégorie avec l'ID " + cat.getId() + " n'existe pas !")))
                    .collect(Collectors.toSet());

            // On remplace les entités "transientes" du JSON par les entités "managed" de la BDD
            bookEntity.setCategories(managedCategories);
        }

        // 2. Sauvegarde dans PostgreSQL
        BookEntity savedEntity = bookPostgresRepository.save(bookEntity);

        // 3. Conversion et Dénormalisation vers OpenSearch
        BookDocument document = new BookDocument();
        document.setId(savedEntity.getId());
        document.setTitle(savedEntity.getTitle());
        document.setContent(savedEntity.getContent());
        document.setAuthor(savedEntity.getAuthor());

        if (savedEntity.getCategories() != null) {
            List<String> categoryNames = savedEntity.getCategories().stream()
                    .map(CategoryEntity::getName)
                    .toList();
            document.setCategories(categoryNames);
        }

        // 4. Indexation OpenSearch
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
