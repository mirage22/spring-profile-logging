package com.mirowengner.example.spring.controller;

import com.mirowengner.example.spring.model.VehicleModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * VehicleShopController
 *
 * @author Miroslav Wengner (@miragemiko)
 */
@RestController
@RequestMapping(value = "/shop")
public class VehicleShopController {

    private final AtomicInteger counter = new AtomicInteger();
    private final Map<Integer, VehicleModel> vehicles = new HashMap<>();

    @RequestMapping(value = "/models", method =
            RequestMethod.GET,
            produces = {"application/json"})
    @ResponseBody
    public Collection<VehicleModel> vehiclesGet() {
        return vehicles.values();
    }

    @RequestMapping(value = "/models/vehicle", method =
            RequestMethod.POST,
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public VehicleModel vehiclePost(@RequestBody VehicleModel vehicle) {
        final int nextId = counter.getAndIncrement();
        vehicle.setId(nextId);
        vehicles.put(nextId, vehicle);
        return vehicle;
    }

}
