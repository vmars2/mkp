package com.shravya.mkp.entities;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by syakkali on 08/10/17.
 */
public class ProjectCollector {

    private SessionFactory sessionFactory;

    public ProjectCollector(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Project> getProjectsWithDeadlineUnder(long threshold) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Project> projects;

        try {
            Criteria criteria = session.createCriteria(Project.class);
            criteria.add(Restrictions.le("deadline", threshold));
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
