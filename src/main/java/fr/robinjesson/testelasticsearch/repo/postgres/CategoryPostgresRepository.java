package fr.robinjesson.testelasticsearch.repo.postgres;

import fr.robinjesson.testelasticsearch.model.postgres.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryPostgresRepository extends JpaRepository<CategoryEntity, Long> {
}