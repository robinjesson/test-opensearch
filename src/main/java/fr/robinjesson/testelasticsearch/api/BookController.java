package fr.robinjesson.testelasticsearch.api;

import fr.robinjesson.testelasticsearch.model.opensearch.BookDocument;
import fr.robinjesson.testelasticsearch.model.postgres.BookEntity;
import fr.robinjesson.testelasticsearch.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public BookEntity createBook(@RequestBody BookEntity bookEntity) {
        return bookService.saveBook(bookEntity);
    }

    @GetMapping("/opensearch/search")
    public Page<BookDocument> searchOpenSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return bookService.searchBooksWithOpensearch(query, page, size);
    }

    @GetMapping("/hibernate/search")
    public Page<BookEntity> searchHibernate(@RequestParam String query,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        return bookService.searchBooksWithHibernate(query, page, size);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PutMapping("/{id}")
    public BookEntity updateBook(@PathVariable Long id, @RequestBody BookEntity bookEntity) {
        return bookService.updateBook(id, bookEntity);
    }

}