package com.shravya.mkp.entities;

import com.codahale.metrics.annotation.Timed;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
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

    @Path("/sessionFactory")
    @GET
    @Timed
    public Collection<Long> getProjectsSessionFactory(@QueryParam("lower") long lower
            , @QueryParam("higher") long higher) {

        List<Long> projectIds = new ArrayList<>();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
//            long lower = 1508320000;
//            long higher = 1508320613;

            Criteria criteria = session.createCriteria(Project.class);
            criteria.add(Restrictions.ge("deadline", lower));
            criteria.add(Restrictions.le("deadline", higher));

            List<Project> list = criteria.list();

            for(Project project: list) {
                projectIds.add(project.getId());
                StringBuilder builder = new StringBuilder();
                builder.append("Bids for project: " + project.getId() + "\n");
                for(Bid bid: project.getBids()) {
                    builder.append(bid.getId() + "\n");
                }
                System.out.println(builder.toString());
            }

        } finally {
            transaction.commit();
            session.close();
        }

        return projectIds;
    }


}

