package com.sismatix.Elmizan.Model;

public class Media_images_model {

    String images,old_img;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getOld_img() {
        return old_img;
    }

    public void setOld_img(String old_img) {
        this.old_img = old_img;
    }

    public Media_images_model(String images, String old_img) {

        this.images = images;
        this.old_img = old_img;
    }
}
