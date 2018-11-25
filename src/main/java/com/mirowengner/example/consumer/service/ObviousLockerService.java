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

package com.mirowengner.example.consumer.service;

import com.mirowengner.example.consumer.model.VehicleModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ObviousLockerService provides an obvious dead-lock
 *
 * @author Miroslav Wengner (@miragemiko)
 */
@Service(value = "locker")
public class ObviousLockerService implements DefaultService {

    private static final int NUMBER = 1000;

    private final AtomicInteger counter = new AtomicInteger(0);
    private boolean active = true;
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    public ObviousLockerService() {
    }

    @Override
    public void process() {
        int number = counter.getAndIncrement();
        WorkerThread workerThread = new WorkerThread(active);
        workerThread.setName("locker-worker" + number);
        workerThread.setDaemon(true);
        LockerThread first = new LockerThread(active);
        LockerThread second = new LockerThread(active);
        first.setDaemon(true);
        first.setName("locker-1st-" + number);
        second.setDaemon(true);
        second.setName("locker-2st" + number);

        first.init(lock1, lock2);
        second.init(lock2, lock1);

        workerThread.start();
        first.start();
        second.start();
    }

    private static class WorkerThread extends Thread {

        private Map<Integer, VehicleModel> cache = new HashMap<>();
        private boolean active;

        private WorkerThread(boolean active) {
            this.active = active;
        }

        public void run() {
            while (active) {
                Thread.yield();
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < 40 * NUMBER; i++) {
                    VehicleModel m = new VehicleModel(i, "vehicle" + i);
                    cache.put(m.getId(), m);
                }

            }
        }
    }

    private static class LockerThread extends Thread {
        private boolean active;
        private Object l1;
        private Object l2;

        private LockerThread(boolean active) {
            super();
            this.active = active;
        }

        void init(Object l1, Object l2) {
            this.l1 = l1;
            this.l2 = l2;
        }

        public void run() {
            while (active) {
                synchronized (l1) {
                    try {
                        Thread.sleep(NUMBER);
                    } catch (InterruptedException e) {
                    }
                    synchronized (l2) {
                        try {
                            Thread.sleep(NUMBER);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }

    }
}
