package fr.robinjesson.testelasticsearch.repo;

import fr.robinjesson.testelasticsearch.model.opensearch.BookDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface BookRepository extends ElasticsearchRepository<BookDocument, Long> {

    @Query("""
        {
          "multi_match": {
            "query": "?0",
            "fields": ["title", "content"],
            "operator": "OR"
          }
        }
        """)
    List<BookDocument> findByTitleContainingOrContentContaining(String title, String content);
}