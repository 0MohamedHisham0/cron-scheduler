package org.project;

import org.project.processor.SchedulerProcessor;

public class Main {
    private static final SchedulerProcessor schedulerProcessor = new SchedulerProcessor();

    public static void main(String[] args) {
        schedulerProcessor.startScan();
    }

}