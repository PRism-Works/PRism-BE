package com.prismworks.prism;

import com.prismworks.prism.interfaces.index.IndexController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({IndexController.class})
public class PrismApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrismApplication.class, args);
    }
}