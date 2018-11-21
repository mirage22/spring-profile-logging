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

package com.mirowengner.example.spring.service;

import com.mirowengner.example.spring.model.VehicleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Collection;

/**
 * SimpleService simple service runs the thread
 *
 * @author Miroslav Wengner (@miragemiko)
 */
@Service(value = "allocation")
public class AllocationService implements DefaultService{

    private static final Logger log = LoggerFactory.getLogger(AllocationService.class);
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final Map<Integer, VehicleModel> map = new HashMap<>();
    private static final int NUMBER_ALLOC = 40000;
    private static class AllocThread extends Thread {


    	AllocThread() {
    		alloc(NUMBER_ALLOC);
    	}
    	
        public void run() {
            long yieldCounter = 0;
            while (true) {
                Thread.yield();
                Collection<VehicleModel> allocModels = map.values();
                for(VehicleModel c: allocModels) {
                    if(!map.containsKey(c.getId())){
                        log.error("this is weird!");
                    }
                    if(++yieldCounter % 1000 == 0){
                        Thread.yield();
                    }
                }
               
            }
        }

        private static void alloc(int number){
            for(int i=0; i<number;i++){
                map.put(i, new VehicleModel(i, "name_"+i));
            }
        }
    }

    @Override
    public void process(){
        AllocThread allocthread = new AllocThread();
        allocthread.setDaemon(true);
        allocthread.setName("alloc-thread-" + counter.getAndIncrement());
        allocthread.start();
    }


}
