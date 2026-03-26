package edu.lab.cdi;

import edu.lab.ejb.JokeStatisticsBean;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

public class JokeShownObserver {

    @Inject
    private JokeStatisticsBean statistics;

    public void onJokeShown(@Observes JokeShownEvent event) {
        statistics.onJokeShown(event.getJoke());
    }
}
