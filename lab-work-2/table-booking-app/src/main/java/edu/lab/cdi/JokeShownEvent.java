package edu.lab.cdi;

import edu.lab.model.Joke;

public class JokeShownEvent {

    private final Joke joke;

    public JokeShownEvent(Joke joke) {
        this.joke = joke;
    }

    public Joke getJoke() {
        return joke;
    }
}
