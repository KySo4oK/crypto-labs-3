package com.pryimak;

import org.apache.commons.math3.random.MersenneTwister;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.pryimak.Mode.*;
import static java.lang.String.valueOf;

@Path("/casino")
public class ExampleResource {

    @Inject
    @RestClient
    CasinoClient client;

    @Inject Cracker cracker;

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
        return client.play(Lcg.name(), id, valueOf(1), valueOf(34689329)).toString();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String solve(@PathParam("id") String id) {
        List<Long> numbers = IntStream.range(0, 3)
                .boxed()
                .map(i -> {
                    long realNumber = client.play(Lcg.name(), id, valueOf(1), valueOf(34689329)).getRealNumber();
                    System.out.println("realNumber - " + realNumber);
                    return realNumber;
                })
                .collect(Collectors.toList());

//        List<Integer> numbers = List.of(-787081310, -154488167, -120325340);
        
        long modulus = 4294967296L;
        long multiplier = cracker.crackMultiplier(numbers, modulus);
        long increment = cracker.crackIncrement(numbers, modulus, multiplier);
        long bet = cracker.makeBet(numbers, modulus, multiplier, increment);

        BetResult play = client.play(Lcg.name(), id, valueOf(100000), valueOf(bet));
        System.out.println("play" + play);
        //multiplier - 1664525
        //increment - -3281063073

        return play.toString();
    }

    @GET
    @Path("/mt/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String solveMt(@PathParam("id") String id) {
        int beforeCreating = (int) Instant.now().atOffset(ZoneOffset.UTC).toEpochSecond();
        System.out.println(beforeCreating);
        Account acc = client.createAcc(id);
        System.out.println("acc - " + acc);
        BetResult play = client.play(Mt.name(), id, valueOf(1), valueOf(1));
        System.out.println("play - " + play);

        for (int i = 0; i < 100; i++) {
            MersenneTwister mersenneTwister = new MersenneTwister();
            mersenneTwister.setSeed(beforeCreating + i);
            int nextInt = mersenneTwister.nextInt();
            System.out.println(nextInt);
            if (nextInt == play.getRealNumber()) {
                return client.play(Mt.name(), id, valueOf(1), valueOf(mersenneTwister.nextInt())).toString();
            }
        }
        return "";
    }

    @GET
    @Path("/better-mt/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String solveBetterMt(@PathParam("id") String id) {
        client.createAcc(id);

        int[] states = new int[625];
        BetterMtCracker betterMtCracker = new BetterMtCracker();
        for (int i = 0; i < 624; i++) {
            long realNumber = client.play(BetterMt.name(), id, valueOf(1), valueOf(1)).getRealNumber();
            states[i] = (int) realNumber;
        }

        final MT19937_32 clone = betterMtCracker.createNewGeneratorByStates(states);

        BetResult play2 = client.play(BetterMt.name(), id, valueOf(1), valueOf(clone.nextInt()));

        return play2.toString();
    }

}