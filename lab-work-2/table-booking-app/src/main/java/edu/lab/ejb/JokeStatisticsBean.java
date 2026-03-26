package edu.lab.ejb;

import edu.lab.model.Joke;
import jakarta.ejb.Singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class JokeStatisticsBean {

    private final AtomicInteger totalShown = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, AtomicInteger> perJokeShown = new ConcurrentHashMap<>();

    public void onJokeShown(Joke joke) {
        totalShown.incrementAndGet();
        if (joke != null) {
            perJokeShown.computeIfAbsent(joke.getId(), k -> new AtomicInteger(0))
                    .incrementAndGet();
        }
    }

    public Map<String, Object> getStats() {
        Map<Integer, Integer> perJoke = perJokeShown.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().get()
                ));

        return Map.of(
                "totalShown", totalShown.get(),
                "perJokeShown", perJoke
        );
    }
}
