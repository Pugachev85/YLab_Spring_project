package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final Storage storage;

    public UserServiceImpl(UserMapper userMapper, Storage storage) {
        this.userMapper = userMapper;
        this.storage = storage;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        // сгенерировать идентификатор
        // создать пользователя
        // вернуть сохраненного пользователя со всеми необходимыми полями id

        UserEntity newUser = userMapper.userDtoToUserEntity(userDto);
        newUser.setId(storage.getLastUserId() + 1);
        return userMapper.userEntityToUserDto(storage.saveNewUser(newUser));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserEntity user = userMapper.userDtoToUserEntity(userDto);
        return userMapper.userEntityToUserDto(storage.updateUser(user));
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.userEntityToUserDto(storage.getUserById(id));
    }

    @Override
    public void deleteUserById(Long id) {
        storage.deleteUserById(id);
    }
}
