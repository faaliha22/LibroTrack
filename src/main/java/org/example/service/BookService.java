package org.example.service;

import org.example.entity.Book;
import org.example.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {

        String title = book.getTitle().trim();
        String author = book.getAuthor().trim();
        String isbn = book.getIsbn().trim();

        if (bookRepository.existsByIsbn(isbn)) {
            throw new IllegalArgumentException(
                    "A book with this ISBN already exists."
            );
        }

        if (book.getTotalCopies() < 1) {
            throw new IllegalArgumentException(
                    "Total copies must be at least 1."
            );
        }

        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);

        // When a new book is added,
        // all copies are initially available.
        book.setAvailableCopies(book.getTotalCopies());

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}