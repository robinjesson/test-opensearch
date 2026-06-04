package fr.robinjesson.testelasticsearch.repo.postgres;

import fr.robinjesson.testelasticsearch.model.postgres.BookEntity; // <-- On utilise l'Entity
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookPostgresRepository extends JpaRepository<BookEntity, Long> {

    @EntityGraph(attributePaths = {"categories"})
    List<BookEntity> findByTitleContainingOrContentContainingIgnoreCase(String title, String content);
}
