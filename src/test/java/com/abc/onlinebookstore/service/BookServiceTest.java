package com.abc.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.abc.onlinebookstore.dto.BookRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abc.onlinebookstore.dto.BookResponseDTO;
import com.abc.onlinebookstore.entity.BookEntity;
import com.abc.onlinebookstore.exception.BookNotFoundException;
import com.abc.onlinebookstore.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void shouldGetAllBooks() {

        BookEntity book = new BookEntity();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setPrice(new BigDecimal("499.99"));

        when(bookRepository.findAll())
                .thenReturn(List.of(book));

        List<BookResponseDTO> result =
                bookService.getAllBooks();

        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
        assertEquals("Robert C. Martin", result.get(0).getAuthor());
        assertEquals(new BigDecimal("499.99"), result.get(0).getPrice());
    }

    @Test
    void shouldGetBookById() {

        BookEntity book = new BookEntity();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setPrice(new BigDecimal("499.99"));

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        BookResponseDTO result =
                bookService.getBookById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Clean Code", result.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {

        when(bookRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> bookService.getBookById(99L)
        );
    }
    @Test
    void shouldCreateBook() {

        BookRequestDTO request = new BookRequestDTO();
        request.setTitle("Clean Code");
        request.setAuthor("Robert C. Martin");
        request.setPrice(new BigDecimal("499.99"));

        BookEntity savedBook = new BookEntity();
        savedBook.setId(1L);
        savedBook.setTitle("Clean Code");
        savedBook.setAuthor("Robert C. Martin");
        savedBook.setPrice(new BigDecimal("499.99"));

        when(bookRepository.save(org.mockito.ArgumentMatchers.any(BookEntity.class)))
                .thenReturn(savedBook);

        BookResponseDTO result = bookService.createBook(request);

        assertEquals(1L, result.getId());
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthor());
        assertEquals(new BigDecimal("499.99"), result.getPrice());
    }
    @Test
    void shouldUpdateBook() {

        BookEntity book = new BookEntity();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setPrice(new BigDecimal("499.99"));

        BookRequestDTO request = new BookRequestDTO();
        request.setTitle("Clean Code Updated");
        request.setAuthor("Robert C. Martin");
        request.setPrice(new BigDecimal("599.99"));

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(bookRepository.save(book))
                .thenReturn(book);

        BookResponseDTO result =
                bookService.updateBook(1L, request);

        assertEquals("Clean Code Updated", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthor());
        assertEquals(new BigDecimal("599.99"), result.getPrice());
    }
    @Test
    void shouldDeleteBook() {

        BookEntity book = new BookEntity();
        book.setId(1L);
        book.setTitle("Clean Code");

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository).delete(book);
    }
}