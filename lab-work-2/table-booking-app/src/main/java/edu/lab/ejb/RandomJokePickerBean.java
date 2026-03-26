package edu.lab.ejb;

import edu.lab.model.Joke;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Stateless
public class RandomJokePickerBean {

    @Inject
    private JokeRepositoryBean repository;

    public Joke pickRandom() {
        List<Joke> jokes = repository.getAll();
        if (jokes.isEmpty()) {
            return null;
        }
        int idx = ThreadLocalRandom.current().nextInt(jokes.size());
        return jokes.get(idx);
    }
}
