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
