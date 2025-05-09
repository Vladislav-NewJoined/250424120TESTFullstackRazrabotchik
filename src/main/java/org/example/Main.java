
package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class Main {
    public static void main(String[] args) {
        System.out.println("My project has started");
        System.out.println("Здесь смотрим результаты (здесь deploy проекта): " +
                "http://localhost:8080/");

        SpringApplication.run(Main.class, args);
    }
}
