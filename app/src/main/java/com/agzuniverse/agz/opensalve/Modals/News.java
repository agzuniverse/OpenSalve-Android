package com.agzuniverse.agz.opensalve.Modals;

public class News {
    private String body;
    private String author;

    public News(String body, String author) {
        this.body = body;
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }
}
