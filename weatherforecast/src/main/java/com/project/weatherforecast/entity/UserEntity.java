package com.project.weatherforecast.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users_db")
public class UserEntity {
    @Id
    private Integer userid;
    private String username;
    private String pwd;
}
