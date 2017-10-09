package com.shravya.mkp.deadline.processor;

import com.shravya.mkp.entities.Bid;
import com.shravya.mkp.entities.Project;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Processes the projects and bids whose deadline has been reached.
 */
public class DeadlineProcessor implements Runnable {

    private SessionFactory sessionFactory;
    private List<Project> projects;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadlineProcessor.class);

    public DeadlineProcessor(SessionFactory sessionFactory, List<Project> projects) {
        this.sessionFactory = sessionFactory;
        this.projects = projects;
    }

    public void run() {
        Collections.sort(projects, Comparator.comparing((Project project) -> project.getDeadline()));

        while (!projects.isEmpty()) {
            if (projects.get(0).getDeadline() <= Instant.now().getEpochSecond()) {
                Project project = projects.remove(0);
                evaluateProject(project, sessionFactory);
                LOGGER.info("Completed evaluation of project with id: " + project.getId());
            }
        }
    }

    private void acceptBid(Bid bid, Session session) {
        if (bid != null) {
            Query q = session.createQuery("update Bid set status=1 where id=:i");
            q.setParameter("i", bid.getId());

            int status = q.executeUpdate();

            if (status > 0) {
                LOGGER.info("Accepted bid with id: " + bid.getId());
            }
        }
    }

    private void rejectBid(Bid bid, Session session) {
        if (bid != null) {
            Query q = session.createQuery("update Bid set status=2 where id=:i");
            q.setParameter("i", bid.getId());

            int status = q.executeUpdate();

            if (status > 0) {
                LOGGER.info("Rejected bid with id: " + bid.getId());
            }
        }
    }

    private void closeProject(Project project, Session session) {
        Query q = session.createQuery("update Project set status=1 where id=:i");
        q.setParameter("i", project.getId());

        int status = q.executeUpdate();
        if (status > 0) {
            LOGGER.info("Closed project with id: " + project.getId());
        }
    }

    private void evaluateProject(Project project, SessionFactory sessionFactory) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            if (project.getBids() != null) {
                for (Bid bid : project.getBids()) {
                    rejectBid(bid, session);
                }
                acceptBid(project.getBestBid(), session);
                closeProject(project, session);
            }
        } finally {
            transaction.commit();
            session.close();
        }
    }

}
