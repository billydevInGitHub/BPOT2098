package billydev;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
//@EnableConfigurationProperties(TODProperties.class)
public class TOD {
    public static void main(String[] args) {
        SpringApplication.run(TOD.class);
    }



}
