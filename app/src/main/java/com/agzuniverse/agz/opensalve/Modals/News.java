package com.agzuniverse.agz.opensalve.Modals;

public class News {
    private String body;
    private String author;
    private int id;

    public News(String body, String author, int id) {
        this.body = body;
        this.author = author;
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }
}
