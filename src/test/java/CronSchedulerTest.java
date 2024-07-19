import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.model.Job;
import org.project.service.CronScheduler;
import org.project.utils.Constants;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CronSchedulerTest {
    private CronScheduler cronScheduler;
    private Job job;
    private final ZonedDateTime now = ZonedDateTime.now(Constants.zone);
    private final int DEFAULT_WAIT_TIME = 10000;

    @BeforeEach
    void setUp() {
        cronScheduler = CronScheduler.getInstance();
        job = new Job(() -> System.out.println("Job executed"), now.getMinute() + " * * * *", UUID.randomUUID().toString());
    }

    @Test
    void testScheduleJob() {
        cronScheduler.schedule(job);
        assertTrue(cronScheduler.getQueue().contains(job), "Job should be in the queue after scheduling");
    }

    @Test
    void testJobRunning() throws InterruptedException {
        cronScheduler.schedule(job);
        ZonedDateTime expectedRunAt = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        Thread.sleep(DEFAULT_WAIT_TIME);
        Optional<Job> retrievedJob = cronScheduler.getQueue().stream()
                .filter(j -> j.getJobId().equals(job.getJobId()))
                .findFirst();

        retrievedJob.ifPresent(job1 -> {
            if (job1.getLastRunDate() != null) {
                ZonedDateTime lastRunDate = job1.getLastRunDate().truncatedTo(ChronoUnit.MINUTES);
                assertEquals(expectedRunAt, lastRunDate, "Job did not run at the expected time.");
            } else {
                fail("Job's last run date is null.");
            }
        });
    }

    @Test
    void testGetJobsToRun() {
        cronScheduler.schedule(job);
        List<Job> jobsToRun = cronScheduler.getJobsToRun(now);
        assertTrue(jobsToRun.contains(job), "Job should be ready to run");
    }

}
