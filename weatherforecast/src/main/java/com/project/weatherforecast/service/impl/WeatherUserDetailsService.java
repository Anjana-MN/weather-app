package com.project.weatherforecast.service.impl;

import com.project.weatherforecast.entity.UserEntity;
import com.project.weatherforecast.bean.UserPrincipal;
import com.project.weatherforecast.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeatherUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional<UserEntity> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            System.out.println("User 404");
            throw new UsernameNotFoundException("User Not Found");
        }
        return new UserPrincipal(user.get());

    }
}
