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
import com.mirowengner.example.consumer.model.VehicleElement;
import io.opentracing.contrib.spring.tracer.configuration.TracerRegisterAutoConfiguration;
import io.opentracing.contrib.spring.web.starter.ServerTracingAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * StorageApp is the simple app that represent storage of car elements
 *
 * @author Miroslav Wengner (@miragemiko)
 */

@EnableAutoConfiguration(exclude = {TracerRegisterAutoConfiguration.class, ServerTracingAutoConfiguration.class})
@EnableScheduling
@RestController
@Import(value = CustomTracerConfig.class)
@RequestMapping(value = "/storage")
public class StorageApp {


    public static void main(String[] args) {
        SpringApplication.run(StorageApp.class, args);
    }

    public static final String NAME_ELEMENT = "standardElement";

    private static final Map<Integer, VehicleElement> storage = new HashMap<>();
    private static final Random random = new Random();
    private static final List<String> colors = Arrays.asList("black", "white", "blue", "green");


    @RequestMapping(value = "/element", method =
            RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE},
            consumes = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public VehicleElement elementGet(@RequestParam(value = "id") Integer id) {
        return storage.containsKey(id) ? storage.get(id) : createElement(NAME_ELEMENT, id);
    }

    @RequestMapping(value = "/check", method =
            RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE},
            consumes = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public VehicleElement checkGet(@RequestParam(value = "id") Integer id) {
        return storage.get(id);
    }

    private VehicleElement createElement(String name, Integer id) {
        VehicleElement element = new VehicleElement();
        element.setId(id);
        element.setName(name);
        element.setColor(colors.get(random.nextInt(colors.size())));
        storage.putIfAbsent(element.getId(), element);
        return element;
    }
}
