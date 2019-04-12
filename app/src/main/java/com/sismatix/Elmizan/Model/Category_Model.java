package com.sismatix.Elmizan.Model;

public class Category_Model {
    String category_id,category_title;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public Category_Model(String category_id, String category_title) {

        this.category_id = category_id;
        this.category_title = category_title;
    }
}
