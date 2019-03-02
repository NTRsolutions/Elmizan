package com.sismatix.Elmizan.Model;

public class Article_model {
    String date;
    String name;
    String detail;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Article_model(String date, String name, String detail) {

        this.date = date;
        this.name = name;
        this.detail = detail;
    }


}
