package fr.robinjesson.testelasticsearch.repo;

import fr.robinjesson.testelasticsearch.model.opensearch.BookDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookRepository extends ElasticsearchRepository<BookDocument, Long> {
    List<BookDocument> findByTitleContainingOrContentContaining(String title, String content);

    List<BookDocument> findByAuthor(String author);
}
