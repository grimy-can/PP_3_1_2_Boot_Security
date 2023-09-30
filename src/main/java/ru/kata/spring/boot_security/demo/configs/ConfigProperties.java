package ru.kata.spring.boot_security.demo.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import java.time.format.DateTimeFormatter;


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

}