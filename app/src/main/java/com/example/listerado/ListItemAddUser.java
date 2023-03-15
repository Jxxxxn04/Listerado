package com.example.listerado;

public class ListItemAddUser {

    private String user_id, username, image;

    public ListItemAddUser(String user_id, String username, String image) {
        this.user_id = user_id;
        this.username = username;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }
}