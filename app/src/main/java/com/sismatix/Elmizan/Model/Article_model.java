package com.sismatix.Elmizan.Model;

public class Article_model {
    String article_id;
    String article_title;
    String article_description;
    String image;
    String article_status;
    String article_date;
    public Article_model(String article_id, String article_title,
                         String article_description, String image,
                         String article_status, String article_date) {

        this.article_id = article_id;
        this.article_title = article_title;
        this.article_description = article_description;
        this.image = image;
        this.article_status = article_status;
        this.article_date = article_date;
    }


    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_description() {
        return article_description;
    }

    public void setArticle_description(String article_description) {
        this.article_description = article_description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArticle_status() {
        return article_status;
    }

    public void setArticle_status(String article_status) {
        this.article_status = article_status;
    }

    public String getArticle_date() {
        return article_date;
    }

    public void setArticle_date(String article_date) {
        this.article_date = article_date;
    }




}
