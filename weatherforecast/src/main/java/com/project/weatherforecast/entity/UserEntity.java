package com.project.weatherforecast.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Entity;
@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private String username;
    private String password;
}
