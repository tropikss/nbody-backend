package org.acme.resource;

import org.acme.service.NBodyService;
import org.acme.model.Body;
import org.acme.repository.BodyRepository;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import java.util.List;


@Path("/nbody")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NBodyResource {
    @Inject NBodyService nBodyService;
    @Inject BodyRepository bodyRepository;

    @GET
    @Path("/compute")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Body> getBodies() {
        return nBodyService.computeNextStep();
    }
}