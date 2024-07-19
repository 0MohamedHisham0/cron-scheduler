# Scheduled Task Executor

This project is a Java-based scheduled task executor that allows you to schedule and run tasks using a cron expression. The tasks are managed in a priority queue and executed by a thread pool. This solution also includes logging using Log4j2 and unit tests to ensure functionality.

## Features

Features
- **Schedule Function:** Schedule your runnable function using a job ID and cron expression (UNIX format).
- **Priority Queue:** Tasks are added to a priority queue that sorts them by the next execution time.
- **Thread Management:** A dedicated thread checks and executes tasks when their scheduled time arrives, with a sleep interval of 30 seconds.
- **Thread Pool:** Tasks are executed using a thread pool with a configurable number of threads (default: 10).
- **Logging:** Comprehensive logging using Log4j2.
- **Annotations:** Use annotations to specify cron expressions and job IDs for your functions.

## Technical Decisions and Trade-offs

- **Priority Queue:** The initial capacity of the priority queue is set to 10 (PRIORITY_QUEUE_INITIAL_CAPACITY = 10) to handle a reasonable number of tasks. This can be adjusted as needed.
- **Thread Sleep Interval:** The thread checks for tasks every 30 seconds. This trade-off balances availability and performance.
- **Thread Pool:** The number of threads in the pool is set to 10 (NUMBER_OF_POOL_THREADS = 10) to manage concurrent task execution. This number can be configured based on system resources and requirements.

# Usage

### Annotating a Function <br/>
You can use the @Scheduled annotation to execute a new job. *The function must be parameterless*.

```java
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
```
### Using the Schedule Function <br/>
You can also use the schedule function as shown below:

```java
String jobId = UUID.randomUUID().toString();
String cronExpression = "1 * * * *";
Job job = new Job(() -> {
    // your code here
},
        cronExpression,
        jobId
);
cronScheduler.schedule(job);
```
# Conclusion
This scheduled task executor provides a robust and flexible solution for managing and executing tasks in a timely manner. With features like priority queue management, thread pooling, and comprehensive logging, it ensures efficient and reliable task execution. The use of annotations and a straightforward scheduling function makes it easy to integrate into your Java applications. This tool is ideal for applications that require precise scheduling and execution of background tasks.
