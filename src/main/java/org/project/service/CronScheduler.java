package org.project.service;

import com.cronutils.parser.CronParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.project.model.Job;
import org.project.utils.Constants;
import org.project.utils.Functions;
import org.project.utils.JobComparator;
import org.slf4j.MDC;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import static org.project.utils.Constants.*;

public class CronScheduler {
    private static final Logger logger = LogManager.getLogger(CronScheduler.class);
    private final CronParser parser = new CronParser(cronDefinition);
    private final PriorityBlockingQueue<Job> tasksQueue = new PriorityBlockingQueue<>(PRIORITY_QUEUE_INITIAL_CAPACITY, new JobComparator(parser));
    private final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_POOL_THREADS);
    private static final CronScheduler INSTANCE = new CronScheduler();

    private CronScheduler() {
        startPolling();
    }

    public static CronScheduler getInstance() {
        return INSTANCE;
    }

    private void startPolling() {
        Thread pollingThread = new Thread(() -> {
            while (true) {
                try {
                    ZonedDateTime now = ZonedDateTime.now(zone);
                    List<Job> jobsToRun = getAvailableJobsForRunning(now);
                    for (Job jobToRun : jobsToRun) {
                        executor.submit(() -> runJob(jobToRun));
                    }

                    delay(POLLING_THREAD_DELAY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("An error occurred: ", e);
                }
            }
        });

        pollingThread.start();
    }

    public List<Job> getAvailableJobsForRunning(ZonedDateTime now) {
        List<Job> jobsToRun = new ArrayList<>();
        ZonedDateTime nowTruncated = now.truncatedTo(ChronoUnit.MINUTES);
        Job job = tasksQueue.peek();

        while (job != null) {
            ZonedDateTime jobLastRunDateTruncated = null;
            if (job.getLastRunDate() != null) {
                jobLastRunDateTruncated = job.getLastRunDate().truncatedTo(ChronoUnit.MINUTES);
            }

            boolean isCronMatch = Functions.getCronExecutionTime(job.getCronExpression(), parser).isMatch(now);
            boolean isNotRunRecently = jobLastRunDateTruncated == null || !jobLastRunDateTruncated.isEqual(nowTruncated);
            if (isCronMatch && isNotRunRecently) {
                tasksQueue.poll();
                jobsToRun.add(job);
                job = tasksQueue.peek();
            } else {
                break;
            }
        }

        return jobsToRun;
    }

    public void runJob(Job jobToRun) {
        try {
            MDC.put(Constants.LOG_KEYS.JOB_ID, jobToRun.getJobId());
            logger.info("Start Running Job");

            long startTime = System.currentTimeMillis();
            jobToRun.getRunnable().run();
            long endTime = System.currentTimeMillis();

            MDC.put(Constants.LOG_KEYS.TIME_IN_MS, String.format("%d", (endTime - startTime)));
            logger.info("Job Finished");

            MDC.clear();
        } finally {
            jobToRun.setLastRunDate(ZonedDateTime.now());
            System.out.println(jobToRun);

            schedule(jobToRun);
        }
    }

    private void delay(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    public void schedule(Job job) {
        tasksQueue.add(job);
    }

    public PriorityBlockingQueue<Job> getQueue() {
        return tasksQueue;
    }
}
