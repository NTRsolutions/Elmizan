package com.sismatix.Elmizan.Model;

public class Country_model {
    String country_id;
    String country_name;
    String country_status;
    String country_image_url;

    public Country_model(String country_id, String country_name, String country_status, String country_image_url) {

        this.country_id = country_id;
        this.country_name = country_name;
        this.country_status = country_status;
        this.country_image_url = country_image_url;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_status() {
        return country_status;
    }

    public void setCountry_status(String country_status) {
        this.country_status = country_status;
    }

    public String getCountry_image_url() {
        return country_image_url;
    }

    public void setCountry_image_url(String country_image_url) {
        this.country_image_url = country_image_url;
    }


}
