package fr.robinjesson.testelasticsearch.api;

import fr.robinjesson.testelasticsearch.model.opensearch.BookDocument;
import fr.robinjesson.testelasticsearch.model.postgres.BookEntity;
import fr.robinjesson.testelasticsearch.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books/opensearch")
@RequiredArgsConstructor
public class BookOpensearchController {

    private final BookService bookService;

    @PostMapping
    public BookEntity createBook(@RequestBody BookEntity bookEntity) {
        return bookService.saveBook(bookEntity);
    }

    @GetMapping
    public Page<BookDocument> searchOpenSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return bookService.searchBooksWithOpensearch(query, page, size);
    }


    @GetMapping("/{id}")
    public BookDocument getBookById(@PathVariable Long id) {
        return bookService.getBookByIdWithOpensearch(id);
    }


}