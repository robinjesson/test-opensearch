package fr.robinjesson.testelasticsearch.repo.postgres;

import fr.robinjesson.testelasticsearch.model.postgres.BookEntity; // <-- On utilise l'Entity
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookPostgresRepository extends JpaRepository<BookEntity, Long> {

    @Query("""
        SELECT b FROM BookEntity b
        LEFT JOIN FETCH b.categories c
        WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    Page<BookEntity> findByTitleContainingOrContentContainingIgnoreCase(String query, Pageable page);

    @Query("SELECT b FROM BookEntity b LEFT JOIN FETCH b.categories WHERE b.id = :id")
    Optional<BookEntity> findByIdWithCategories(@Param("id") Long id);
}
