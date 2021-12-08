package com.pryimak;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;

@ApplicationScoped
@Path("/casino")
@Produces("application/json")
@Consumes("application/json")
@RegisterRestClient(configKey = "casino")
public interface CasinoClient {

    @GET
    @Path("/createacc")
    Account createAcc(@QueryParam("id") String playerId) throws RuntimeException;

    @GET
    @Path("/play{Mode}")
    BetResult play(@PathParam("Mode") String mode,
                   @QueryParam("id") String playerId,
                   @QueryParam("bet") int amountOfMoney,
                   @QueryParam("number") int theNumberYouBetOn);

}
