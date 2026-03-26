package edu.lab.model;

import java.io.Serializable;

public class Joke implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String text;

    public Joke() {
    }

    public Joke(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
