package com.proxsoftware.webapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@SpringBootApplication
@PropertySource("classpath:app.properties")
@ComponentScan(basePackages = {"com.proxsoftware.webapp.*"})
public class Application implements EmbeddedServletContainerCustomizer {

    @Value(value = "file_uri")
    private static String FILE_URI;



    public static void main(String[] args) {
        System.out.println("BeforeFrom run " + FILE_URI);
        SpringApplication.run(Application.class, args);
        System.out.println("AfterFrom run " + FILE_URI);

    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        System.out.println("from costomize");
        container.setPort(8083);
    }
}
