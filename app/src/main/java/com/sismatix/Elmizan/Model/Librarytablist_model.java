package com.sismatix.Elmizan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Librarytablist_model {


    private String category_id;

    private String category_title;


    public Librarytablist_model(String category_id, String category_title) {
        this.category_id = category_id;
        this.category_title = category_title;
    }

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




}
