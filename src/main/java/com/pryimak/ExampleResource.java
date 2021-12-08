package com.pryimak;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/casino")
public class ExampleResource {

    @Inject
    @RestClient
    CasinoClient client;

    @GET
    @Path("/create/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String create(@PathParam("id") String id) {
        return client.createAcc(id).toString();
    }

    @GET
    @Path("/bet/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String bet(@PathParam("id") String id) {
        return client.play(Mode.Lcg.name(), id, 1, 34689329).toString();
    }
}