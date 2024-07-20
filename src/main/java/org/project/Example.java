package org.project;

import org.project.model.Job;
import org.project.processor.Scheduled;
import org.project.processor.SchedulerProcessor;
import org.project.service.CronScheduler;

import java.util.UUID;

public class Example {
    private static final CronScheduler cronScheduler = CronScheduler.getInstance();
    private static final SchedulerProcessor schedulerProcessor = new SchedulerProcessor();

    public static void main(String[] args) {
        exampleScheduledFunction();
        schedulerProcessor.startScan();
    }

    private static void exampleScheduledFunction() {
        String uuid = UUID.randomUUID().toString();

        Runnable runnable = () -> {
            try {
                Thread.sleep(1000);
                System.out.println("Job Executed");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Job job = new Job(runnable,
                "35 * * * *",
                uuid
        );
        cronScheduler.schedule(job);
    }

    @Scheduled(cronExpression = "35 * * * *", jobId = "jobId")
    public void exampleScheduled(){
        try {
            Thread.sleep(1000);
            System.out.println("Job Executed");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
