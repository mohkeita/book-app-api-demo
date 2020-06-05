package io.mohkeita.bookappapi.service;

import io.mohkeita.bookappapi.dao.BookRepository;
import io.mohkeita.bookappapi.dto.BookRequest;
import io.mohkeita.bookappapi.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
