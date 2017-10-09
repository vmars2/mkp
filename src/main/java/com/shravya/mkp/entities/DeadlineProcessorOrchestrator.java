package com.shravya.mkp.entities;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

/**
 * Created by syakkali on 08/10/17.
 */
public class DeadlineProcessorOrchestrator implements Runnable {

    private SessionFactory sessionFactory;
    private int deadlineThreshold;  //In minutes
    private ProjectCollector projectCollector;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeadlineProcessorOrchestrator.class);

    public DeadlineProcessorOrchestrator(SessionFactory sessionFactory, int deadlineThreshold) {
        this.sessionFactory = sessionFactory;
        this.deadlineThreshold = deadlineThreshold;
        this.projectCollector = new ProjectCollector(sessionFactory);
    }

    public void run() {
        while(true) {

            long now = Instant.now().getEpochSecond();
            long threshold = now;

            List<Project> projects = projectCollector.getProjectsWithDeadlineUnder(threshold);
            if(projects.isEmpty()) {
                LOGGER.info("No projects found with deadline within: " + threshold);
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append("projects with deadline within: " + threshold + "\n");
                for(Project project: projects) {
                    builder.append(project.getId() + "\n");
                }
                LOGGER.info(builder.toString());
                (new Thread(new DeadlineProcessor(sessionFactory, projects))).start();
            }

            try {
                Thread.sleep(deadlineThreshold * 60 * 1000);
            } catch (InterruptedException e) {
                LOGGER.info("DeadlineProcessorOrchestrator sleep between checks interrupted", e);
            }
        }
    }


}
