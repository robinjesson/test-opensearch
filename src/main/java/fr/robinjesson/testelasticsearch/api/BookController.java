package fr.robinjesson.testelasticsearch.controller;

import fr.robinjesson.testelasticsearch.model.opensearch.BookDocument;
import fr.robinjesson.testelasticsearch.model.postgres.BookEntity;
import fr.robinjesson.testelasticsearch.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<BookDocument> searchOpenSearch(@RequestParam String query) {
        return bookService.searchBooksWithOpensearch(query);
    }

    @GetMapping("/hibernate/search")
    public List<BookEntity> searchHibernate(@RequestParam String query) {
        return bookService.searchBooksWithHibernate(query);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PutMapping("/{id}")
    public BookEntity updateBook(@PathVariable Long id, @RequestBody BookEntity bookEntity) {
        return bookService.updateBook(id, bookEntity);
    }

    @PostMapping("/opensearch/index")
    public void updateBook() {
        bookService.reindexAllBooks();
    }

}