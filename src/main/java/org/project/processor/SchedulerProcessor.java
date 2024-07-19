package org.project.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.project.model.Job;
import org.project.service.CronScheduler;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SchedulerProcessor {
    private static final Logger logger = LogManager.getLogger(CronScheduler.class);
    private final CronScheduler cronScheduler = CronScheduler.getInstance();

    public SchedulerProcessor() {}

    public void startScan() {
        String packageName = "org.project";
        try {
            Class<?>[] classes = getClassesInPackage(packageName);
            for (Class<?> clazz : classes) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Scheduled.class)) {
                        if (method.getParameterCount() > 0) {
                            throw new IllegalArgumentException("Method " + method.getName() + " in class " + clazz.getName() + " has parameters, which is not allowed.");
                        }
                        Job job = getJob(clazz, method);
                        cronScheduler.schedule(job);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("An error occurred: ", e);
        }
    }

    public static Job getJob(Class<?> clazz, Method method) {
        Scheduled scheduled = method.getAnnotation(Scheduled.class);
        String groupId = scheduled.groupId();
        String cronExpression = scheduled.cronExpression();

        Job job = new Job();
        job.setJobId(groupId);
        job.setCronExpression(cronExpression);
        job.setRunnable(() -> {
            try {
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                logger.error("An error occurred: ", e);
            }
        });
        return job;
    }

    public static Class<?>[] getClassesInPackage(String packageName) {
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);

        if (resource == null) {
            return new Class<?>[0];
        }

        File directory = new File(URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8));
        if (!directory.exists()) {
            return new Class<?>[0];
        }

        List<Class<?>> classes = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    try {
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        logger.error("An error occurred: ", e);
                    }
                }
            }
        }
        return classes.toArray(new Class<?>[0]);
    }
}
