package com.project.weatherforecast.repository;

import com.project.weatherforecast.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Repository
//@Component
public interface UserRepository extends JpaRepository<UserEntity,String> {

    Optional<UserEntity> findByUsername(String username);
}
