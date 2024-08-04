package com.estimator.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import io.github.cdimascio.dotenv.Dotenv;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DotenvConfigTest {

    @Test
    public void testDotenvBeanCreation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DotenvConfig.class);
        Dotenv dotenv = context.getBean(Dotenv.class);
        assertNotNull(dotenv, "Dotenv bean should be created");
    }
}
