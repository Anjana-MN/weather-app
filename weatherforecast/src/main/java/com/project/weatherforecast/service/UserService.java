package com.project.weatherforecast.service;

import com.project.weatherforecast.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

public interface UserService {

    UserEntity addUser(UserEntity user);

    List<UserEntity> fetchUsers();

//    Optional<UserEntity> fetchUser(String username);
}
