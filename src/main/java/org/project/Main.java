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

        ZonedDateTime now = ZonedDateTime.now();
        String job2Expr = now.getMinute() + " * * * *";

        for (int i = 0; i < 3; i++) {
            String uuid = UUID.randomUUID().toString();
            Job job2 = new Job(
                    getRunnableForTest(),
                    job2Expr,
                    uuid
            );
            CronScheduler.getInstance().schedule((job2));
        }
    }

    public static Runnable getRunnableForTest() {
        return () -> {
            for (int i = 1; i < 3; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

//    @Scheduled(groupId = "myGroup", cronExpression = "16 * * * *")
//    public void runThread() {
//        for (int i = 1; i < 3; i++) {
//            try {
//                System.out.println();
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    @Scheduled(groupId = "myGroup", cronExpression = "4 * * * *")
//    public void myTask() {
//        System.out.println("TASK 1");
//    }
}