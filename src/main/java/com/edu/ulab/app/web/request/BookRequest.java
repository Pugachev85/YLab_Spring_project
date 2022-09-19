package com.edu.ulab.app.web.request;

import lombok.Data;

@Data
public class BookRequest {
//    Добавил поле id, так как для апдейта книги нужно сначала достать ее из базы,
//    следовательно id известен. При создании книги id = null, что не мешает нам ее создать
    private Long id;
    private String title;
    private String author;
    private long pageCount;
}
