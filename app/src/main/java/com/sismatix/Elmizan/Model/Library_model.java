package com.sismatix.Elmizan.Model;

public class Library_model {
    String title ,date,detail;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Library_model(String title, String date, String detail) {

        this.title = title;
        this.date = date;
        this.detail = detail;
    }
}
