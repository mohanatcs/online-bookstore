package com.abc.onlinebookstore.service;

import com.abc.onlinebookstore.dto.BookRequestDTO;
import com.abc.onlinebookstore.dto.BookResponseDTO;

import java.util.List;

public interface BookService {

    List<BookResponseDTO> getAllBooks();

    BookResponseDTO getBookById(Long id);

    BookResponseDTO createBook(BookRequestDTO request);

    BookResponseDTO updateBook(Long id, BookRequestDTO request);

    void deleteBook(Long id);
}