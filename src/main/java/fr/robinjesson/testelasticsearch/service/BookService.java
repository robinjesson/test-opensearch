package fr.robinjesson.testelasticsearch.service;

import fr.robinjesson.testelasticsearch.model.Book;
import fr.robinjesson.testelasticsearch.repo.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingOrContentContaining(query, query);
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}
