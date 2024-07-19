package org.project.utils;

import com.cronutils.model.Cron;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.project.utils.Constants.zone;

public class Functions {
    public static ExecutionTime getCronExecutionTime(String cronExpression, CronParser parser) {
        Cron cron = parser.parse(cronExpression);
        return ExecutionTime.forCron(cron);
    }
}
