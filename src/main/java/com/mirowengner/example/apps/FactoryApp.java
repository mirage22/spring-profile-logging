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

package com.mirowengner.example.apps;

import com.mirowengner.example.consumer.config.CustomTracerConfig;
import com.mirowengner.example.consumer.model.VehicleModel;
import com.mirowengner.example.customer.CustomerApp;
import io.opentracing.contrib.spring.tracer.configuration.TracerRegisterAutoConfiguration;
import io.opentracing.contrib.spring.web.starter.ServerTracingAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * FactoryApp represents simple factory that put all pieces together
 * provided by the {@link StorageApp}
 *
 * @author Miroslav Wengner (@miragemiko)
 */
@EnableAutoConfiguration(exclude = {TracerRegisterAutoConfiguration.class, ServerTracingAutoConfiguration.class})
@EnableScheduling
@RestController
@Import(value = CustomTracerConfig.class)
@RequestMapping(value = "/factory")
public class FactoryApp {

    public static final String NAME_VEHICLE = "standardVehicle";

    public static void main(String[] args) {
        SpringApplication.run(FactoryApp.class, args);
    }

    private final AtomicInteger counter = new AtomicInteger();
    private static final Map<Integer, VehicleModel> vehicles = new HashMap<>();

    @Value("${tracing.storage.url:http://localhost:8083}")
    private String storageUrl;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/vehicle", method =
            RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE},
            consumes = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public VehicleModel vehicleGet(@RequestParam(value = "id") Integer id) {
        return vehicles.containsKey(id) ? vehicles.get(id) : createVehicle(NAME_VEHICLE, id);
    }

    @RequestMapping(value = "/check", method =
            RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE},
            consumes = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public VehicleModel checkGet(@RequestParam(value = "id") Integer id) {
        return vehicles.get(id);
    }


    @RequestMapping(value = "/production", method =
            RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE},
            consumes = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public VehicleModel productionGet() {
        return createVehicle(NAME_VEHICLE, counter.getAndIncrement());
    }



    @RequestMapping(value = "/create", method =
            RequestMethod.POST,
            produces = {APPLICATION_JSON_VALUE},
            consumes = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public VehicleModel createVehiclePost(@RequestBody VehicleModel vehicle) {
        Integer nextId = counter.getAndIncrement();
        vehicle.setId(counter.getAndIncrement());
        vehicles.putIfAbsent(nextId, vehicle);
        return vehicle;
    }

    private VehicleModel createVehicle(String name, Integer id) {
        VehicleModel vehicle = new VehicleModel(id, name + id);
        vehicles.putIfAbsent(id, vehicle);
        return vehicle;
    }

}
