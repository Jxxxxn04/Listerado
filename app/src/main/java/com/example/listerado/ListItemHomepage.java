package com.example.listerado;

public class ListItemHomepage {

    String product_id, product_name, category_id, category_name;

    public ListItemHomepage(String product_id, String product_name, String category_id, String category_name) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.category_id = category_id;
        this.category_name = category_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_id() {
        return category_id;
    }
}
