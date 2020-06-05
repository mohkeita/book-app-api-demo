package io.mohkeita.bookappapi.dao;

import io.mohkeita.bookappapi.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
