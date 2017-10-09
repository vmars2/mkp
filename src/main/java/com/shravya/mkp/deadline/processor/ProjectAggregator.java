package com.shravya.mkp.deadline.processor;

import com.shravya.mkp.entities.Project;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Aggregates projects with deadline within a threshold
 */
public class ProjectAggregator {

    private SessionFactory sessionFactory;

    public ProjectAggregator(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Project> getProjectsWithDeadlineWithinCutOff(long cutOff) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Project> projects;

        try {
            Criteria criteria = session.createCriteria(Project.class);
            criteria.add(Restrictions.le("deadline", cutOff));
            criteria.add(Restrictions.eq("status", Project.Status.OPEN));
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            projects = criteria.list();
        } finally {
            transaction.commit();
            session.close();
        }

        return projects;
    }

}
