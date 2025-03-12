package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.entity.UserEntity;
import com.project.weatherforecast.repository.UserRepository;
import com.project.weatherforecast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override public UserEntity addUser(UserEntity user) {
        user.setPwd(encoder.encode(user.getPwd()));
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> fetchUsers() {
        List<UserEntity> users = userRepository.findAll();
//        users.forEach(user ->{
//            String decodedPwd = user.getPwd().
//        });
        return users;
    }

    public Optional<UserEntity> fetchUser(String username){
        return userRepository.findByUsername(username);
    }
}
