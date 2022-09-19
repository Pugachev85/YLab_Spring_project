package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final Storage storage;

    public BookServiceImpl(BookMapper bookMapper, Storage storage) {
        this.bookMapper = bookMapper;
        this.storage = storage;
    }


    @Override
    public BookDto createBook(BookDto bookDto) {
        BookEntity newBook = bookMapper.bookDtoToBookEntity(bookDto);
        newBook.setId(storage.getLastBookId() + 1);
        return bookMapper.bookEntityToBookDto(storage.saveNewBook(newBook));
    }

    @Override
    public List<BookDto> updateBooks(List<BookDto> booksForUpdate, Long userId) {
        Map<Long, BookEntity> booksInBase = storage.findAllBooksOfUser(userId);
        log.info("booksInBase: {}", booksInBase);

        for (BookDto book : booksForUpdate) {
            if (booksInBase.containsKey(book.getId())) {
                storage.updateBook(bookMapper.bookDtoToBookEntity(book));
            } else {
                storage.saveNewBook(bookMapper.bookDtoToBookEntity(book));
            }
        }

        List<Long> updatedBooksId = booksForUpdate.stream()
                .filter(Objects::nonNull)
                .map(BookDto::getId)
                .toList();
        log.info("BooksInBaseId: {}", updatedBooksId);

        booksInBase.keySet().forEach(key -> {
            if (!updatedBooksId.contains(key)) storage.deleteBook(key);
        });

        return storage.findAllBooksOfUser(userId)
                .values()
                .stream()
                .filter(Objects::nonNull)
                .peek(updatedBook -> log.info("Updated in storage book: {} ", updatedBook))
                .map(bookMapper::bookEntityToBookDto)
                .toList();

    }

    @Override
    public List<BookDto> getBooksByUserId(Long userId) {
        return storage.findAllBooksOfUser(userId)
                .values()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookEntityToBookDto)
                .toList();
    }

    @Override
    public void deleteBooksByUserId(Long userId) {
        storage.findAllBooksOfUser(userId)
                .keySet()
                .forEach(storage::deleteBook);
        log.info("Books in base after delete: {}", storage.findAllBooksOfUser(userId));
    }
}
