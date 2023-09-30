package ru.kata.spring.boot_security.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user")
@Builder
public class User {

    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private int age;

    @Column(name = "alias")
    private String alias;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "is_alive")
    private Boolean isAlive;

    @Column(name = "regdate")
    private String regdate;

    @Column(name = "edited")
    private String edited;

}