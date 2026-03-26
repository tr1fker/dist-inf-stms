package edu.lab.ejb;

import edu.lab.model.Joke;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public class JokeRepositoryBean {

    private final List<Joke> jokes = new ArrayList<>();

    @Inject
    @Named("jokes")
    private List<Joke> initialJokes;

    @PostConstruct
    void init() {
        jokes.clear();
        if (initialJokes != null) {
            jokes.addAll(initialJokes);
        }
    }

    public List<Joke> getAll() {
        return Collections.unmodifiableList(jokes);
    }
}
