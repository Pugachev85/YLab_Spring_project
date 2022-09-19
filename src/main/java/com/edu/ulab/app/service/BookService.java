package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto createBook(BookDto bookDto);

    List<BookDto> updateBooks(List<BookDto> bookDto, Long userId);

    List<BookDto> getBooksByUserId(Long id);

    void deleteBooksByUserId(Long userId);
}
