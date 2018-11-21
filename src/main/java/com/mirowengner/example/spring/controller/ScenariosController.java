package com.mirowengner.example.spring.controller;

import com.mirowengner.example.spring.service.DefaultService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * ScenariosController simple scenario
 *
 * @author Miroslav Wengner (@miragemiko)
 */

@RestController
@RequestMapping(value = "/scenario")
public class ScenariosController {

    private final DefaultService lockerService;

    @Autowired
    public ScenariosController(@Qualifier(value = "locker") DefaultService lockerService) {
        this.lockerService = lockerService;
    }

    @RequestMapping(value = "/deadlock", method = RequestMethod.GET)
    public String run(){
        lockerService.process();

        return "Running";
    }

}
