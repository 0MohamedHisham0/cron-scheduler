package org.project.utils;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;

import java.time.ZoneId;

public interface Constants {
    CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX);
    ZoneId zone = ZoneId.systemDefault();
    int NUMBER_OF_POOL_THREADS = 10;
    int PRIORITY_QUEUE_INITIAL_CAPACITY = 11;
    int POLLING_THREAD_DELAY = 30000;
    interface LOG_KEYS {
        String TIME_IN_MS = "timeInMs";
        String JOB_ID = "jobId";
    }
}
