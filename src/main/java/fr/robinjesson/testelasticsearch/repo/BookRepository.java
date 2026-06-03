package fr.robinjesson.testelasticsearch.repo;

import fr.robinjesson.testelasticsearch.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookRepository extends ElasticsearchRepository<Book, String> {
    List<Book> findByTitleContainingOrContentContaining(String title, String content);

    List<Book> findByAuthor(String author);
}
