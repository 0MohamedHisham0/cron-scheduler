package org.project;

import org.project.processor.Scheduled;
import org.project.processor.SchedulerProcessor;
import org.project.model.Job;
import org.project.service.CronScheduler;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Main {
    private static final SchedulerProcessor schedulerProcessor = new SchedulerProcessor();

    public static void main(String[] args) {
        schedulerProcessor.startScan();
    }

    @Scheduled(groupId = "groupId1", cronExpression = "16 * * * *")
    public void runThread() {
        for (int i = 1; i < 3; i++) {
            try {
                System.out.println();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Scheduled(groupId = "groupId2", cronExpression = "4 * * * *")
    public void myTask() {
        System.out.println("TASK 1");
    }
}