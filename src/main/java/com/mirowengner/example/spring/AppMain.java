package com.mirowengner.example.spring;

import io.opentracing.Tracer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

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
public class AppMain {

    public static final int PORT = 8081;

    public static void main(String[] args) {
        new SpringApplicationBuilder(AppMain.class)
                .web(WebApplicationType.SERVLET)
                .run( "--spring.application.name=sample-backend",
                "--server.port="+PORT);
    }
}
