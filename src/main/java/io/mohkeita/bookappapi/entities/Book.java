package io.mohkeita.bookappapi.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String author;
    private String isbn;

}
