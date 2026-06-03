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

    @GetMapping("/search")
    public List<BookDocument> search(@RequestParam String query) {
        return bookService.searchBooks(query);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}