package fr.robinjesson.testelasticsearch.repo.opensearch;

import fr.robinjesson.testelasticsearch.model.opensearch.BookDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface BookRepository extends ElasticsearchRepository<BookDocument, Long> {

    @Query("""
        {
          "query_string": {
            "query": "?0",
            "fields": ["title", "content"],
            "default_operator": "OR",
            "analyze_wildcard": true
          }
        }
        """)
    List<BookDocument> findByTitleContainingOrContentContaining(String title, String content);
}