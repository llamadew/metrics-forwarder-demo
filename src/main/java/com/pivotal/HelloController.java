package com.pivotal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by pivotal on 6/13/17.
 */
@RestController
public class HelloController {

    private static final Random RANDOM = new SecureRandom();
    private static final int default_max_random_delay = 6000;

    private final Timer timer;

    HelloController(MetricRegistry metricRegistry) {
        this.timer = metricRegistry.timer("timer.response.test");
    }

    @RequestMapping("/timertest")
    String test(
                @RequestHeader(value = "Authorization", required=false, defaultValue = "NOVALUEPROVIDED") String auth_header,
                @RequestParam(value="time", required = false, defaultValue="-1") int time, 
                @RequestParam(value="min", required = false, defaultValue="-1") int min_time,
                @RequestParam(value="max", required = false, defaultValue="-1") int max_time) throws InterruptedException {

        try (Timer.Context context = this.timer.time()) {
            System.out.println("Auth Header: "+auth_header);
	    int delay = RANDOM.nextInt(default_max_random_delay);

            if(time > -1)
                delay = time; 
            else if(min_time > -1 && max_time > -1){
                delay = RANDOM.ints(1l, min_time, max_time).sum();
            }
            else if(min_time > -1 ){    
                delay = RANDOM.ints(1l, min_time, Integer.MAX_VALUE).sum();
            }else if (max_time > -1){
                delay = RANDOM.ints(1l,0, max_time).sum();
            }            
            Thread.sleep(delay-(context.stop()/1000000));
            return "{\"target_delay\":"+String.valueOf(delay) +
                    ", \"measured_delay\": " + context.stop()/1000000 +"}";
        }
    }

    @RequestMapping(value = "/")
    public String sayHello() {
        return "Hello";
    }
}
