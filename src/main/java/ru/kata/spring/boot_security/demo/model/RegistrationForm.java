package ru.kata.spring.boot_security.demo.model;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;

@Data
public class RegistrationForm {

    @Size(min=2, max=64)
    @NotEmpty(message = "Обязательное поле")
    private String email;

    @Size(min=8, max=32)
    @NotEmpty(message = "Минимум 8, максимум 32 знака")
    private String password;

    @NotEmpty(message = "Обязательное поле")
    private String name;

    @NotEmpty(message = "Обязательное поле")
    private String lastName;

    @NotNull
    @Min(value = 18, message = "18+")
    @Digits(integer=3, fraction=0, message = "Не более 3-х знаков")
    private int age;

    private String occupation;

    private boolean haveLicense;
    public User toUser(PasswordEncoder passwordEncoder) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setName(name);
        newUser.setLastName(lastName);
        newUser.setAge(age);
        newUser.setOccupation(occupation);
        newUser.setHaveLicense(haveLicense);
        return newUser;
    }
}
