package org.project.utils;

import com.cronutils.model.Cron;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import org.project.model.Job;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;

import static org.project.utils.Constants.zone;

public class JobComparator implements Comparator<Job> {
    private final CronParser parser;

    public JobComparator(CronParser parser) {
        this.parser = parser;
    }

    @Override
    public int compare(Job job1, Job job2) {
        ZonedDateTime now = ZonedDateTime.now(zone).truncatedTo(ChronoUnit.MINUTES);

        ExecutionTime executionTime1 = getExecutionTime(job1);
        ExecutionTime executionTime2 = getExecutionTime(job2);

        Optional<ZonedDateTime> nextExecution1 = executionTime1.nextExecution(now);
        Optional<ZonedDateTime> nextExecution2 = executionTime2.nextExecution(now);

        if (nextExecution1.isEmpty() || nextExecution2.isEmpty()) return 0;

        ZonedDateTime nextExecutionTime1 = getZonedDateTime(job1, nextExecution1, executionTime1, now);
        ZonedDateTime nextExecutionTime2 = getZonedDateTime(job2, nextExecution2, executionTime2, now);

        return nextExecutionTime1.isBefore(nextExecutionTime2) ? -1 : 1;
    }

    private static ZonedDateTime getZonedDateTime(Job job1, Optional<ZonedDateTime> nextExecution1, ExecutionTime executionTime1, ZonedDateTime now) {
        ZonedDateTime nextExecutionTime1 = nextExecution1.get().truncatedTo(ChronoUnit.MINUTES);
        ZonedDateTime lastRunDate1 = job1.getLastRunDate();
        if (lastRunDate1 != null) {
            lastRunDate1 = lastRunDate1.truncatedTo(ChronoUnit.MINUTES);
        }

        if (executionTime1.isMatch(now) && (lastRunDate1 == null || !lastRunDate1.isEqual(now))) {
            nextExecutionTime1 = executionTime1.lastExecution(now).get().truncatedTo(ChronoUnit.MINUTES);
        }
        return nextExecutionTime1;
    }

    private ExecutionTime getExecutionTime(Job job1) {
        String expression1 = job1.getCronExpression();
        Cron cron1 = parser.parse(expression1);
        return ExecutionTime.forCron(cron1);
    }
}
