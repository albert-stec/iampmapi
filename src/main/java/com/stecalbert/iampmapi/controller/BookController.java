package com.stecalbert.iampmapi.controller;

import com.stecalbert.iampmapi.model.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    List<Book> books = Arrays.asList(
            new Book(1L, "Hobbit", "J.R.R. Tolkien", 1937),
            new Book(2L, "Mitologia nordycka", "Gaiman Neil", 2017),
            new Book(3L, "Mroczna krypta", "Schwab V. E.", 2021),
            new Book(1L, "Silmarillion", "J.R.R. Tolkien", 2004)
    );

    @GetMapping
    @CrossOrigin
    public ResponseEntity getAll() {
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    @CrossOrigin
    public ResponseEntity findById(@PathVariable String id) {
        Long idL = Long.valueOf(id);
        Book book = books.stream().filter(e -> e.getId().equals(idL)).findFirst().orElseThrow(BookNotFoundException::new);

        return ResponseEntity.ok(book);
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public class BookNotFoundException extends RuntimeException {
    }
}
