package net.kanzi.kz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KzApplication {

    public static void main(String[] args) {
        SpringApplication.run(KzApplication.class, args);
    }

}
