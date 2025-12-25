package kz.soft.finalrpo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class FinalrpoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalrpoApplication.class, args);
        System.out.println(new BCryptPasswordEncoder().encode("admin123"));

    }

}
