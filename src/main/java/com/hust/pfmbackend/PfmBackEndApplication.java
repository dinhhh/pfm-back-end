package com.hust.pfmbackend;

import com.hust.pfmbackend.scheduler.DebtSchedule;
import com.hust.pfmbackend.scheduler.RecurringTransactionSchedule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class PfmBackEndApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(PfmBackEndApplication.class, args);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new DebtSchedule(context),
                0,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new RecurringTransactionSchedule(context),
                0,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }

}
