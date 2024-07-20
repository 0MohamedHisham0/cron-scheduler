import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.processor.Scheduled;
import org.project.processor.SchedulerProcessor;
import org.project.model.Job;

import java.lang.reflect.Method;


public class SchedulerProcessorTest {
    private static final String mockedJobId = "testGroupId";
    private static final String mockedCronExpression = "12 * * * *";

    @BeforeEach
    void setUp() {
        SchedulerProcessor schedulerProcessor = new SchedulerProcessor();
        schedulerProcessor.startScan();
    }

    @Test
    void testAnnotationProcessing() throws Exception {
        Method testMethod = MockScheduledClass.class.getMethod("scheduledMethod");
        Job job = SchedulerProcessor.getJob(MockScheduledClass.class, testMethod);

        Assertions.assertEquals(mockedJobId, job.getJobId());
        Assertions.assertEquals(mockedCronExpression, job.getCronExpression());
    }

    static class MockScheduledClass {
        @Scheduled(jobId = mockedJobId, cronExpression = mockedCronExpression)
        public void scheduledMethod() {

        }
    }

}

