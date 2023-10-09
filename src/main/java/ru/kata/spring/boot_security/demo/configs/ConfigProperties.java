package ru.kata.spring.boot_security.demo.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Data
@Configuration
@PropertySource("classpath:application.properties")
public class ConfigProperties {

    @Value("${dateformat.value}")
    private String formatDateTime;

    @Bean
    public DateTimeFormatter getDateTimeFormatter(){
        return  DateTimeFormatter.ofPattern(formatDateTime);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    Map<String, String> getRoleTargetUrlMap() {
        Map<String, String> roleTargetUrlMap = new HashMap<>();
        roleTargetUrlMap.put("ROLE_ADMIN", "/admin/users");
        roleTargetUrlMap.put("ROLE_USER", "/authenticated");
        return roleTargetUrlMap;
    }
}