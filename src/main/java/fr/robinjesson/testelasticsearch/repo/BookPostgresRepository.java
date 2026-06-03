package fr.robinjesson.testelasticsearch.repo;

import fr.robinjesson.testelasticsearch.model.postgres.BookEntity; // <-- On utilise l'Entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookPostgresRepository extends JpaRepository<BookEntity, Long> {
}
