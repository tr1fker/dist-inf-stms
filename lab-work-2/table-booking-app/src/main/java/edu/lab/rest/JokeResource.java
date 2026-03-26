package edu.lab.rest;

import edu.lab.cdi.JokeShownEvent;
import edu.lab.cdi.Logged;
import edu.lab.ejb.JokeRepositoryBean;
import edu.lab.ejb.JokeStatisticsBean;
import edu.lab.ejb.UserJokeHistoryBean;
import edu.lab.model.Joke;
import edu.lab.service.JokeGenerator;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SessionScoped
@Path("joke")
@Produces(MediaType.APPLICATION_JSON)
public class JokeResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private JokeRepositoryBean repository;

    @Inject
    private UserJokeHistoryBean history;

    @Inject
    private JokeGenerator generator;

    @Inject
    private Event<JokeShownEvent> jokeShownEvent;

    @Inject
    private JokeStatisticsBean statistics;

    @GET
    @Path("all")
    public List<Joke> all() {
        return repository.getAll();
    }

    @GET
    @Path("random")
    @Logged
    public Response random() {
        Joke joke = generator.nextJoke();
        if (joke == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "No jokes available"))
                    .build();
        }
        history.add(joke);
        jokeShownEvent.fire(new JokeShownEvent(joke));
        return Response.ok(joke).build();
    }

    @GET
    @Path("history")
    public Map<String, Object> history() {
        List<Joke> jokes = history.getHistory();
        return Map.of(
                "history", jokes,
                "count", jokes.size(),
                "globalStats", statistics.getStats()
        );
    }
}
