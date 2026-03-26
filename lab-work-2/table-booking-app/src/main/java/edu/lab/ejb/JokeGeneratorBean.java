package edu.lab.ejb;

import edu.lab.cdi.Logged;
import edu.lab.model.Joke;
import edu.lab.service.JokeGenerator;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class JokeGeneratorBean implements JokeGenerator {

    @Inject
    private RandomJokePickerBean picker;

    @Override
    @Logged
    public Joke nextJoke() {
        return picker.pickRandom();
    }
}
