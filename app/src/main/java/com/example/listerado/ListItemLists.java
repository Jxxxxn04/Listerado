package com.example.listerado;

public class ListItemLists {
    private String text, id, owner_username, owner_id;

    public ListItemLists(String text, String id, String owner_username, String owner_id) {
        this.text = text;
        this.id = id;
        this.owner_username = owner_username;
        this.owner_id = owner_id;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getOwner_username() {
        return owner_username;
    }

    public String getOwner_id() {
        return owner_id;
    }
}
