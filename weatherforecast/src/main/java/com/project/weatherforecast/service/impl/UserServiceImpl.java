package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.entity.UserEntity;
import com.project.weatherforecast.repository.UserRepository;
import com.project.weatherforecast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override public UserEntity addUser(UserEntity user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
