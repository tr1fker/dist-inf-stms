package edu.lab.cdi;

import edu.lab.model.Joke;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import java.util.List;

public class JokeListProducer {

    @Produces
    @Named("jokes")
    public List<Joke> produceJokes() {
        return List.of(
                new Joke(1, "— Почему программисты не любят природу? — Там слишком много багов."),
                new Joke(2, "Сначала был код. Потом пришли требования и всё переписали."),
                new Joke(3, "— У тебя есть план на день? — Да: компилировать и не плакать."),
                new Joke(4, "Работает? Не трогай. Не работает? Не трогай, само сломалось."),
                new Joke(5, "Лучший алгоритм сортировки — спросить у Stack Overflow, как правильно.")
        );
    }
}
