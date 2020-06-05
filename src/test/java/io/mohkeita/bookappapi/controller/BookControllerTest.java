package io.mohkeita.bookappapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mohkeita.bookappapi.dto.BookRequest;
import io.mohkeita.bookappapi.entities.Book;
import io.mohkeita.bookappapi.exception.BookNotFoundException;
import io.mohkeita.bookappapi.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Captor
    private ArgumentCaptor<BookRequest> argumentCaptor;


    @Test
    public void postingANewBookShouldCreateANewBookInTheDatabase() throws Exception {

        BookRequest bookRequest = new BookRequest();
        bookRequest.setAuthor("Mohamed");
        bookRequest.setTitle("Java 13");
        bookRequest.setIsbn("4041");

        when(bookService.createNewBook(argumentCaptor.capture())).thenReturn(1L);

        this.mockMvc
                .perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/books/1"));

        assertThat(argumentCaptor.getValue().getAuthor(), is("Mohamed"));
        assertThat(argumentCaptor.getValue().getIsbn(), is("4041"));
        assertThat(argumentCaptor.getValue().getTitle(), is("Java 13"));
    }

    @Test
    public void allBooksEndpointShouldReturnTwoBooks() throws Exception {

        when(bookService.getAllBooks()).thenReturn(List.of(createBook(1L, "Java 11", "Mohamed", "4041"),
                createBook(2L, "Java EE 8", "Don Salim", "42")));

        this.mockMvc
                .perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Java 11")))
                .andExpect(jsonPath("$[0].author", is("Mohamed")))
                .andExpect(jsonPath("$[0].isbn", is("4041")))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getBookWithIdOneShouldReturnABook() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(createBook(1L, "Java 11", "Mohamed", "4041"));

        this.mockMvc
                .perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.title", is("Java 11")))
                .andExpect(jsonPath("$.author", is("Mohamed")))
                .andExpect(jsonPath("$.isbn", is("4041")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getBookWithUnknownIdShouldReturn404() throws Exception {

        when(bookService.getBookById(42L)).thenThrow(new BookNotFoundException("Book with id '42' not found"));

        this.mockMvc
                .perform(get("/api/books/42"))
                .andExpect(status().isNotFound());
    }

    private Book createBook(Long id, String title, String author, String isbn) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setId(id);
        book.setIsbn(isbn);

        return book;
    }
}
