package org.project.model;


import java.time.ZonedDateTime;
import java.util.UUID;


public class Job {
    private Runnable runnable;
    private String cronExpression;
    private String jobId = UUID.randomUUID().toString();
    private ZonedDateTime lastRunDate = null;

    public Job(Runnable runnable, String cronExpression, String jobId) {
        this.runnable = runnable;
        this.cronExpression = cronExpression;
        this.jobId = jobId;
    }

    public Job(Runnable runnable, String cronExpression) {
        this.runnable = runnable;
        this.cronExpression = cronExpression;
    }

    public Job() {

    }

    public ZonedDateTime getLastRunDate() {
        return lastRunDate;
    }

    public void setLastRunDate(ZonedDateTime lastRunDate) {
        this.lastRunDate = lastRunDate;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

}