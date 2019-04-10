package com.sismatix.Elmizan.Model;

public class Premium_Lawyer_Model {
    String lawyer_image,old_img;


    public String getOld_img() {
        return old_img;
    }

    public void setOld_img(String old_img) {
        this.old_img = old_img;
    }

    public Premium_Lawyer_Model(String lawyer_image, String old_img) {

        this.lawyer_image = lawyer_image;
        this.old_img = old_img;
    }

    public String getLawyer_image() {
        return lawyer_image;
    }

    public void setLawyer_image(String category_name) {
        this.lawyer_image = category_name;
    }


}
