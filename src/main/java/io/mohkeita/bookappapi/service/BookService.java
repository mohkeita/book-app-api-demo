package io.mohkeita.bookappapi.service;

import io.mohkeita.bookappapi.dao.BookRepository;
import io.mohkeita.bookappapi.dto.BookRequest;
import io.mohkeita.bookappapi.entities.Book;
import io.mohkeita.bookappapi.exception.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;


    public Long createNewBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setAuthor(bookRequest.getAuthor());
        book.setTitle(bookRequest.getTitle());
        book.setIsbn(bookRequest.getIsbn());

        book = bookRepository.save(book);

        return book.getId();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        Optional<Book> requestBook = bookRepository.findById(id);

        if (requestBook.isEmpty()) {
            throw new BookNotFoundException(String.format("Book with id: '%s' not found ", id));
        }
        return requestBook.get();
    }
}
