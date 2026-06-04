package fr.robinjesson.testelasticsearch.repo.opensearch;

import fr.robinjesson.testelasticsearch.model.opensearch.BookDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface BookOpensearchRepository extends ElasticsearchRepository<BookDocument, Long> {

    @Query("""
        {
          "query_string": {
            "query": "?0",
            "fields": ["title", "categories"],
            "default_operator": "OR",
            "analyze_wildcard": true
          }
        }
        """)
    Page<BookDocument> findByTitleContainingOrCategoriesContaining(String query, Pageable pageable);
}