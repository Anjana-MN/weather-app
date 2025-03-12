package com.project.weatherforecast.controller;

import com.project.weatherforecast.entity.UserEntity;
import com.project.weatherforecast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> getUsers(){
        return ResponseEntity.status(HttpStatus.OK).body("Login Successful");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestBody UserEntity userEntity
    ){
        UserEntity registerdUser = userService.addUser(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registered "+ registerdUser.getUsername());
    }
}
