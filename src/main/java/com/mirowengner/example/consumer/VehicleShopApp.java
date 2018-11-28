/*
 * Copyright (C)  2018 Miroslav Wengner
 *                        http://www.wengnermiro.com/
 *
 *  This software is free:
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESSED OR
 *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *   OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *   IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *   INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *   NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *   DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *   THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *   THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *   Copyright (C) Miroslav Wengner, 2018
 */

package com.mirowengner.example.consumer;

import io.opentracing.contrib.spring.tracer.configuration.TracerRegisterAutoConfiguration;
import io.opentracing.contrib.spring.web.starter.ServerTracingAutoConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Spring ConsumerApp is simple application that represents a VehicleServiceApp.
 * The Vehicle service communicates with the factory service. The Factory service is able
 * to request for appropriate vehicle pieces and create a car. The final car is then stored
 * in Vehicle service. Vehicle service is then request by the Customer app.
 *
 * <p>
 * Jeager is configured to the default port 6831 on localhost
 * Start Jeager docker instance
 * $docker run -d -p 6831:6831/udp -p 16686:16686 -t jaegertracing/all-in-one
 * <p>
 * run local: -Dspring.application.name=vehicle_shop -Dserver.port=8081
 * run docker compose : use environment
 * variables: APPLICATION_NAME=consumer;DEMO_PORT=8081;OPENTRACING_HOST=jaeger;OPENTRACING_PORT=6831
 *
 *
 * @author Miroslav Wengner (@miragemiko)
 */

@SpringBootApplication(exclude = {TracerRegisterAutoConfiguration.class, ServerTracingAutoConfiguration.class})
public class VehicleShopApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(VehicleShopApp.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
