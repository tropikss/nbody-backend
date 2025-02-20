package org.acme.resource;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import java.util.*;

import org.acme.service.NBodyService;
import org.acme.model.Body;

@Path("/nbody")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NBodyResource {
    @Inject NBodyService nBodyService;

    @GET
    @Path("/compute")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Body> getBodies() {
        return nBodyService.computeNextStep();
    }
}