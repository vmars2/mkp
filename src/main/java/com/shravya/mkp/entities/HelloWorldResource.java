package com.shravya.mkp.entities;

import com.codahale.metrics.annotation.Timed;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by syakkali on 08/10/17.
 */
@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    private SessionFactory sessionFactory;

    public HelloWorldResource(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Path("/vanilla")
    @GET
    @Timed
    public Collection<Long> getProjectsVanilla() {
        List<Long> projectIds = new ArrayList<>();

        Project project = new Project();
        project.setId(24);
        project.setName("prj from HelloWorld");
        projectIds.add(project.getId());

        return projectIds;
    }

    @Path("/getProjectsInDeadlineRange")
    @GET
    @Timed
    public Collection<Long> getProjectsInDeadlineRange(@QueryParam("lower") long lower
            , @QueryParam("higher") long higher) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Long> projectIds = new ArrayList<>();
        try {
            List<Project> projects = getProjectsInDeadlineRange2(lower, higher, session);
            for (Project project : projects) {
                projectIds.add(project.getId());
            }
        } finally {
            transaction.commit();
            session.close();
        }
        return projectIds;
    }

    @Path("/closeProjectsInDeadlineRange")
    @GET
    @Timed
    public Collection<Long> closeProjectsInDeadlineRange(@QueryParam("lower") long lower
            , @QueryParam("higher") long higher) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Long> projectIds = new ArrayList<>();

        try {
            List<Project> projects = getProjectsInDeadlineRange2(lower, higher, session);
            evaluateProjects(projects, session);

            for (Project project : projects) {
                projectIds.add(project.getId());
            }
        } finally {
            transaction.commit();
            session.close();
        }
        return projectIds;
    }

    private void acceptBid(Bid bid, Session session) {
        if(bid != null) {
            Query q = session.createQuery("update Bid set status=1 where id=:i");
            q.setParameter("i", bid.getId());

            int status = q.executeUpdate();
            System.out.println(status);
        }
    }

    private void rejectBid(Bid bid, Session session) {
        if(bid != null) {
            Query q = session.createQuery("update Bid set status=2 where id=:i");
            q.setParameter("i", bid.getId());

            int status = q.executeUpdate();
            System.out.println(status);
        }
    }

    private void closeProject(Project project, Session session) {
        Query q = session.createQuery("update Project set status=1 where id=:i");
        q.setParameter("i", project.getId());

        int status = q.executeUpdate();
        System.out.println(status);
    }

    private void evaluateProjects(List<Project> projects, Session session) {
        for (Project project : projects) {
            if (project.getBids() != null) {
                for (Bid bid : project.getBids()) {
                    rejectBid(bid, session);
                }
                acceptBid(project.getBestBid(), session);
                closeProject(project, session);
            }
        }
    }

    private List<Project> getProjectsInDeadlineRange2(long lower, long higher, Session session) {

        List<Project> projects;
//            long lower = 1508320000;
//            long higher = 1508320613;
        Criteria criteria = session.createCriteria(Project.class);
        criteria.add(Restrictions.ge("deadline", lower));
        criteria.add(Restrictions.le("deadline", higher));
        projects = criteria.list();

        return projects;
    }


}

