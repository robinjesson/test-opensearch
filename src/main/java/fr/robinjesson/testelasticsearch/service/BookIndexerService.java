package fr.robinjesson.testelasticsearch.service;

import fr.robinjesson.testelasticsearch.model.opensearch.BookDocument;
import fr.robinjesson.testelasticsearch.model.postgres.BookEntity;
import fr.robinjesson.testelasticsearch.model.postgres.CategoryEntity;
import fr.robinjesson.testelasticsearch.repo.opensearch.BookOpensearchRepository;
import fr.robinjesson.testelasticsearch.repo.postgres.BookPostgresRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookIndexerService {

    private final BookPostgresRepository bookPostgresRepository;
    private final BookOpensearchRepository bookOpensearchRepository; // OpenSearch Repo
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public void indexBook(BookEntity book) {
        BookDocument document = new BookDocument();
        document.setId(book.getId());
        document.setTitle(book.getTitle());
        document.setContent(book.getContent());
        document.setAuthor(book.getAuthor());

        if (book.getCategories() != null) {
            document.setCategories(book.getCategories().stream()
                    .map(CategoryEntity::getName)
                    .toList());
        }

        try {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);

            List<?> revisions = auditReader.createQuery()
                    .forRevisionsOfEntity(BookEntity.class, false, true)
                    .add(AuditEntity.id().eq(book.getId()))
                    .addOrder(AuditEntity.revisionNumber().desc())
                    .getResultList();

            document.setVersionCount(revisions.size());

            if (!revisions.isEmpty()) {
                Object[] latestRevisionData = (Object[]) revisions.get(0);

                String revType = latestRevisionData[2].toString(); // ADD, MOD ou DEL
                document.setLastRevisionType(revType);
            } else {
                document.setLastRevisionType("UNKNOWN");
            }

        } catch (Exception e) {
            log.error("Erreur lors de l'extraction de l'audit Envers pour le livre ID {}", book.getId(), e);
            document.setVersionCount(1);
            document.setLastRevisionType("ADD");
        }

        bookOpensearchRepository.save(document);
    }
}