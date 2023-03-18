package com.example.listerado;

public class ListItemProduct {

    private String list_id, product_id, username, added_date, product_name, category_name;

    public ListItemProduct(String list_id, String product_id, String username, String added_date, String product_name, String category_name) {
        this.list_id = list_id;
        this.product_id = product_id;
        this.username = username;
        this.added_date = added_date;
        this.product_name = product_name;
        this.category_name = category_name;
    }

    public String getUsername() {
        return username;
    }

    public String getAdded_date() {
        return added_date;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getList_id() {
        return list_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }
}
