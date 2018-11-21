package com.mirowengner.example.spring;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Spring AppMain is simple backend application
 *
 * Jeager is configured to the default port 6831 on localhost
 * Start Jeager docker instance
 * $docker run -d -p 6831:6831/udp -p 16686:16686 jaegertracing/all-in-one:latest
 *
 * @author Miroslav Wengner (@miragemiko)
 */

@SpringBootApplication
public class ConsumerApp {

    public static final int PORT = 8081;

    public static void main(String[] args) {
        new SpringApplicationBuilder(ConsumerApp.class)
                .web(WebApplicationType.SERVLET)
                .run( "--spring.application.name=consumer",
                "--server.port="+PORT);
    }
}
