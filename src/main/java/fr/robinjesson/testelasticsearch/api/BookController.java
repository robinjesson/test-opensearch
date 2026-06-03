package fr.robinjesson.testelasticsearch.api;

import fr.robinjesson.testelasticsearch.model.Book;
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
    public Book createBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String query) {
        return bookService.searchBooks(query);
    }

    @DeleteMapping("/{id}")
    public void deleteBooks(@PathVariable String id) {
        bookService.deleteBook(id);
    }
}
