package com.example.listerado;

public class ListItem {
    private String text, id, username;

    public ListItem(String text, String id, String username) {
        this.text = text;
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }
}
