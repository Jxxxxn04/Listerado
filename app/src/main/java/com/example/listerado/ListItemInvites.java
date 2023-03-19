package com.example.listerado;

public class ListItemInvites {
    String list_id, listname, owner_id, owner_username, member_id, member_username;


    public ListItemInvites(String list_id, String listname, String owner_id, String owner_username) {
        this.list_id = list_id;
        this.listname = listname;
        this.owner_id = owner_id;
        this.owner_username = owner_username;
    }


    public String getList_id() {
        return list_id;
    }

    public String getListname() {
        return listname;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public String getOwner_username() {
        return owner_username;
    }
}
