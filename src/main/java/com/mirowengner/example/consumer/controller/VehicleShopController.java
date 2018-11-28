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

package com.mirowengner.example.consumer.controller;

import com.mirowengner.example.consumer.model.VehicleModel;
import com.mirowengner.example.utils.HttpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * VehicleShopController
 *
 * @author Miroslav Wengner (@miragemiko)
 */
@RestController
@RequestMapping(value = "/shop")
public class VehicleShopController {

    private final AtomicInteger counter = new AtomicInteger();
    private final Map<Integer, VehicleModel> soldVehicles = new HashMap<>();

    private final RestTemplate restTemplate;

    @Value("${tracing.factory.url:http://localhost:8084}")
    private String factoryUrl;

    @Autowired
    public VehicleShopController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/sold/vehicles", method =
            RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public Collection<VehicleModel> vehiclesGet() {
        return soldVehicles.values();
    }

    @RequestMapping(value = "/sold/vehicle", method =
            RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE},
            consumes = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public VehicleModel vehicleGet(@RequestParam(value = "id") Integer id) {

        final ResponseEntity<VehicleModel> response = HttpHelper.requestGetVehicleById(restTemplate,
                factoryUrl + "/factory/vehicle?id={id}", id);

        final VehicleModel vehicle = response.getBody();
        if (vehicle == null) {
            return new VehicleModel();
        } else {
            final VehicleModel soldVehicle = soldVehicles.get(vehicle.getId());
            return soldVehicle;
        }
    }

    @RequestMapping(value = "/factory/production", method =
            RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE},
            consumes = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public VehicleModel vehicleProductionGet() {
        final ResponseEntity<VehicleModel> response = HttpHelper.requestGetVehicleById(restTemplate,
                factoryUrl + "/factory/production");
        return response.getBody();
    }

    @RequestMapping(value = "/create/vehicle", method =
            RequestMethod.POST,
            produces = {APPLICATION_JSON_VALUE},
            consumes = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public VehicleModel createVehiclePost(@RequestBody VehicleModel vehicle) {

        final ResponseEntity<VehicleModel> checkResponse = HttpHelper.requestGetVehicleById(restTemplate,
                factoryUrl + "/factory/check?id={id}", vehicle.getId());

        VehicleModel factoryVehicle = checkResponse.getBody();
        if (factoryVehicle == null) {
            VehicleModel createdVehicle = new VehicleModel();
            createdVehicle.setName("customVehicle");
            ResponseEntity<VehicleModel> postResponse = restTemplate
                    .postForEntity(factoryUrl + "/factory/create", createdVehicle, VehicleModel.class);
            factoryVehicle = postResponse.getBody();

        }

        soldVehicles.put(factoryVehicle.getId(), factoryVehicle);
        return factoryVehicle;
    }


    @RequestMapping(value = "/models/vehicle", method =
            RequestMethod.PUT,
            produces = {APPLICATION_JSON_VALUE},
            consumes = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public VehicleModel vehiclePut(@RequestBody VehicleModel vehicle) {
        soldVehicles.replace(vehicle.getId(), vehicle);
        return vehicle;
    }

}
