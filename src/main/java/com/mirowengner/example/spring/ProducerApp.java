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

package com.mirowengner.example.spring;

import com.mirowengner.example.spring.model.VehicleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SimpleClientApp simple client application that calls {@link ConsumerApp}
 *
 * @author Miroslav Wengner (@miragemiko)
 */

@EnableAutoConfiguration
@EnableScheduling
@RestController
public class ProducerApp {

    public static final int PORT = 8082;

    private final AtomicInteger vehicleNumber = new AtomicInteger();
    private final String backendServiceUrl = System.getProperty("com.mirowengner.example.backend.url", "http://localhost:" + ConsumerApp.PORT);

    @Bean
    private RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;


    @Scheduled(fixedRate = 10000)
    public void postNewVehicle() {
        VehicleModel vehicle = new VehicleModel();
        vehicle.setName("vehicle" + vehicleNumber.getAndIncrement());
        ResponseEntity<VehicleModel> response = restTemplate.postForEntity(backendServiceUrl + "/shop/models/vehicle", vehicle, VehicleModel.class);

    }


    @Scheduled(fixedRate = 3000)
    public void getVehicles() {
        ResponseEntity<List> vehicles = restTemplate.getForEntity(backendServiceUrl + "/shop/models", List.class);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/client", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public List<VehicleModel> getShopInfo() {

        final ResponseEntity<List<VehicleModel>> response = restTemplate
                .exchange(backendServiceUrl + "/shop/models", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<VehicleModel>>() {
                });

        return response.getBody();

    }


    public static void main(String[] args) {
        SpringApplication.run(ProducerApp.class,
                "--spring.application.name=producer",
                "--server.port=" + PORT
        );
    }


}
