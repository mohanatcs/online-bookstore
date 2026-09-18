package com.abc.onlinebookstore.service;

import com.abc.onlinebookstore.dto.BookRequestDTO;
import com.abc.onlinebookstore.dto.BookResponseDTO;
import com.abc.onlinebookstore.entity.BookEntity;
import com.abc.onlinebookstore.exception.BookNotFoundException;
import com.abc.onlinebookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookResponseDTO> getAllBooks() {

        return bookRepository.findAll()
                .stream()
                .map(book -> new BookResponseDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPrice()
                ))
                .toList();
    }

    @Override
    public BookResponseDTO getBookById(Long id) {

        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found"));

        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice()
        );
    }
    @Override
    public BookResponseDTO createBook(BookRequestDTO request) {

        BookEntity book = new BookEntity();

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());

        BookEntity savedBook = bookRepository.save(book);

        return new BookResponseDTO(
                savedBook.getId(),
                savedBook.getTitle(),
                savedBook.getAuthor(),
                savedBook.getPrice()
        );
    }
    @Override
    public BookResponseDTO updateBook(Long id, BookRequestDTO request) {

        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found"));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());

        BookEntity updatedBook = bookRepository.save(book);

        return new BookResponseDTO(
                updatedBook.getId(),
                updatedBook.getTitle(),
                updatedBook.getAuthor(),
                updatedBook.getPrice()
        );
    }

    @Override
    public void deleteBook(Long id) {

        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found"));

        bookRepository.delete(book);
    }
}