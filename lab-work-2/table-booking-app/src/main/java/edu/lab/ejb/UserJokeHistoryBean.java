package edu.lab.ejb;

import edu.lab.model.Joke;
import jakarta.ejb.Stateful;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateful
public class UserJokeHistoryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Joke> history = new ArrayList<>();

    public void add(Joke joke) {
        if (joke != null) {
            history.add(joke);
        }
    }

    public List<Joke> getHistory() {
        return new ArrayList<>(history);
    }

    public void clear() {
        history.clear();
    }
}
