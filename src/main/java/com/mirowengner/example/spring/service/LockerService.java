package com.mirowengner.example.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
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
