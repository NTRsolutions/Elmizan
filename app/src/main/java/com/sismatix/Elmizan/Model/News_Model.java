package com.sismatix.Elmizan.Model;

public class News_Model {
    String news_id ,news_title,news_description,news_media,news_status,image,news_date,news_day,news_month,news_year;

    public News_Model(String news_id, String news_title, String news_description, String news_media, String news_status,
                      String image, String news_date, String news_day, String news_month, String news_year) {
        this.news_id = news_id;
        this.news_title = news_title;
        this.news_description = news_description;
        this.news_media = news_media;
        this.news_status = news_status;
        this.image = image;
        this.news_date = news_date;
        this.news_day = news_day;
        this.news_month = news_month;
        this.news_year = news_year;
    }
    public String getNews_day() {
        return news_day;
    }

    public void setNews_day(String news_day) {
        this.news_day = news_day;
    }

    public String getNews_month() {
        return news_month;
    }

    public void setNews_month(String news_month) {
        this.news_month = news_month;
    }

    public String getNews_year() {
        return news_year;
    }

    public void setNews_year(String news_year) {
        this.news_year = news_year;
    }



    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }



    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_description() {
        return news_description;
    }

    public void setNews_description(String news_description) {
        this.news_description = news_description;
    }

    public String getNews_media() {
        return news_media;
    }

    public void setNews_media(String news_media) {
        this.news_media = news_media;
    }

    public String getNews_status() {
        return news_status;
    }

    public void setNews_status(String news_status) {
        this.news_status = news_status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
