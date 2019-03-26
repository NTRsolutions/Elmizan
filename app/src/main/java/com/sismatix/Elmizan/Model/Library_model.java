package com.sismatix.Elmizan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Library_model {


    String library_id;

    private String library_title;

    private String library_link;

    private String category_id;

    private String library_date;

    private String library_status;

    public Library_model(String library_id, String library_title,
                         String library_link, String category_id,
                         String library_date, String library_status,String library_description) {
        this.library_id = library_id;
        this.library_title = library_title;
        this.library_link = library_link;
        this.category_id = category_id;
        this.library_date = library_date;
        this.library_status = library_status;
        this.library_description = library_description;
    }

    public String getLibrary_description() {
        return library_description;
    }

    public void setLibrary_description(String library_description) {
        this.library_description = library_description;
    }

    private String library_description;

    public String getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(String library_id) {
        this.library_id = library_id;
    }

    public String getLibrary_title() {
        return library_title;
    }

    public void setLibrary_title(String library_title) {
        this.library_title = library_title;
    }

    public String getLibrary_link() {
        return library_link;
    }

    public void setLibrary_link(String library_link) {
        this.library_link = library_link;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getLibrary_date() {
        return library_date;
    }



    public void setLibrary_date(String library_date) {
        this.library_date = library_date;
    }

    public String getLibrary_status() {
        return library_status;
    }

    public void setLibrary_status(String library_status) {
        this.library_status = library_status;
    }


}
