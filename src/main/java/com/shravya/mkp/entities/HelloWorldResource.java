package com.shravya.mkp.entities;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    @GET
    @Timed
    public Collection<Long> getProjectsInRange() {
        List<Long> projectIds = new ArrayList<>();

        Project project = new Project();
        project.setId(24);
        project.setName("prj from HelloWorld");

        projectIds.add(project.getId());
        return projectIds;
    }


}

