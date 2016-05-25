package org.rest.webapp.Schedule;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static org.quartz.JobBuilder.newJob;

/**
 * Created by Ivan on 5/21/2016.
 */
public class QuartzListener implements ServletContextListener {

    Scheduler scheduler = null;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {

            System.out.println("Context initialized");
            // Setup the Job class and the Job group
            JobDetail job = newJob(PoemScheduledService.class)
                    .withIdentity("job1", "group1")
                    .build();

            // Trigger the job to run now, and then every 40 seconds
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myTrigger", "group1")
                    .startNow()
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(300)
                                    .repeatForever())
                    .build();

            // schedule it

            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Context Destroyed");
        try
        {
            scheduler.shutdown();
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
        }
    }
}

