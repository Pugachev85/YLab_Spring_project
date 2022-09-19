package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class Storage {
    //todo создать хранилище в котором будут содержаться данные
    // сделать абстракции через которые можно будет производить операции с хранилищем
    // продумать логику поиска и сохранения
    // продумать возможные ошибки
    // учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
    // продумать что у узера может быть много книг и нужно создать эту связь
    // так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции

    private final NavigableMap<Long, UserEntity> usersStorage = new TreeMap<>();
    private final NavigableMap<Long, BookEntity> booksStorage = new TreeMap<>();

    public Long getLastUserId() {
        if (usersStorage.isEmpty()) return 0L;
        return usersStorage.lastKey();
    }

    public Long getLastBookId() {
        if (booksStorage.isEmpty()) return 0L;
        return booksStorage.lastKey();
    }

    public UserEntity saveNewUser(UserEntity user) {
        if (user == null) throw new NotFoundException("User is null");

        if (usersStorage.values().stream()
                .map(UserEntity::getFullName)
                .toList()
                .contains(user.getFullName())) {
            throw new NotFoundException("User with name: \"" + user.getFullName() + "\" not found in storage");
        }
        usersStorage.put(user.getId(), user);
        return usersStorage.get(user.getId());
    }

    public BookEntity saveNewBook(BookEntity book) {
        booksStorage.put(book.getId(), book);
        return booksStorage.get(book.getId());
    }

    public UserEntity updateUser(UserEntity user) {
        if (user == null) throw new NotFoundException("User is null");
        if (!usersStorage.containsKey(user.getId())) throw new NotFoundException("User with Id: "
                + user.getId() + " not found in storage");

        usersStorage.replace(user.getId(), user);
        return usersStorage.get(user.getId());
    }

    public Map<Long, BookEntity> findAllBooksOfUser(Long userId) {
        return booksStorage.values()
                .stream()
                .filter(book -> book.getUserId().equals(userId))
                .collect(Collectors.toMap(BookEntity::getId, BookEntity -> BookEntity));
    }

    public void updateBook(BookEntity bookEntity) {
        if (!booksStorage.containsKey(bookEntity.getId())) throw new NotFoundException("Book with Id: "
                + bookEntity.getId() + " not found in storage");
        booksStorage.replace(bookEntity.getId(), bookEntity);
    }

    public void deleteBook(Long id) {
        if (!booksStorage.containsKey(id)) throw new NotFoundException("Book with Id: "
                + id + " not found in storage");
        booksStorage.keySet().removeIf(curKey -> curKey.equals(id));
    }

    public UserEntity getUserById(Long id) {
        if (!usersStorage.containsKey(id)) throw new NotFoundException("User with Id: "
                + id + " not found in storage");
        return usersStorage.get(id);
    }

    public void deleteUserById(Long id) {
        if (!usersStorage.containsKey(id)) throw new NotFoundException("User with Id: "
                + id + " not found in storage");
        usersStorage.keySet().removeIf(curKey -> curKey.equals(id));
    }
}
