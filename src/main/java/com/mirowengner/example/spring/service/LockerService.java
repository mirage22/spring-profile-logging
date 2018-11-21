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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *
 *
 * @author Miroslav Wengner (@miragemiko)
 */
@Service(value = "locker")
public class LockerService implements DefaultService {

    private static final Logger log = LoggerFactory.getLogger(LockerService.class);

    private final DefaultService allocationService;
    private final AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    public LockerService(@Qualifier(value ="allocation") DefaultService allocationService) {
        this.allocationService = allocationService;
    }

    @Override
    public void process(){
        allocationService.process();
        Object lock1 = new Object();
        Object lock2 = new Object();

        LockerThread first = new LockerThread();
        LockerThread second = new LockerThread();
        first.setDaemon(true);
        int number = counter.getAndIncrement();
        first.setName("locker-1st-" + number);
        second.setDaemon(true);
        second.setName("locker-2st"+ number);

        first.init(lock1, lock2);
        second.init(lock2, lock1);
        
        first.start();
        second.start();
    }


    private static class LockerThread extends Thread {
        Object l1;
        Object l2;

        void init(Object lock1, Object lock2) {
            l1 = lock1;
            l2 = lock2;
        }

        public void run() {
            while (true) {
                synchronized (l1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    synchronized (l2) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        log.info("Got one!");
                    }
                }
            }
        }

    }
    
    
}
