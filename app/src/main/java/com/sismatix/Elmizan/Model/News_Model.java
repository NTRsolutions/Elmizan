package com.sismatix.Elmizan.Model;

public class News_Model {
    String news_date,news_title,news_detail;

    public News_Model(String news_date, String news_title, String news_detail) {
        this.news_date = news_date;
        this.news_title = news_title;
        this.news_detail = news_detail;
    }

    public String getNews_date() {

        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_detail() {
        return news_detail;
    }

    public void setNews_detail(String news_detail) {
        this.news_detail = news_detail;
    }
}
