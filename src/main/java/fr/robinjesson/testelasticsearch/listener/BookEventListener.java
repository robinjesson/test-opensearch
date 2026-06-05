package fr.robinjesson.testelasticsearch.listener;

import fr.robinjesson.testelasticsearch.event.BookEvent;
import fr.robinjesson.testelasticsearch.model.postgres.BookEntity;
import fr.robinjesson.testelasticsearch.repo.opensearch.BookOpensearchRepository;
import fr.robinjesson.testelasticsearch.repo.postgres.BookPostgresRepository;
import fr.robinjesson.testelasticsearch.service.BookIndexerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookEventListener {

    private final BookIndexerService bookIndexerService;
    private final BookPostgresRepository bookPostgresRepository;
    private final BookOpensearchRepository bookOpensearchRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBookChangeEvent(BookEvent event) {
        log.info("Transaction validée dans Postgres pour le livre ID {}. Alignement OpenSearch (Aiven) en cours...", event.bookId());

        try {
            switch (event.action()) {
                case SAVE, UPDATE -> {
                    bookPostgresRepository.findByIdWithCategories(event.bookId())
                            .ifPresentOrElse(
                                    (book) -> {
                                        bookIndexerService.indexBook(book);
                                        log.info("Livre ID {} synchronisé avec succès dans OpenSearch.", event.bookId());
                                    },
                                    () -> log.warn("Livre ID {} introuvable en BDD lors de l'indexation.", event.bookId())
                            );
                }
                case DELETE -> {
                    bookOpensearchRepository.deleteById(event.bookId());
                    log.info("Livre ID {} supprimé avec succès de OpenSearch.", event.bookId());
                }
            }
        } catch (Exception e) {
            log.error("ERREUR de synchronisation OpenSearch pour le livre ID {}. Le filet de sécurité (CRON) devra rejouer cette action.", event.bookId(), e);
        }
    }
}