package com.sismatix.Elmizan.Model;

public class Media_Video_Model {
    String video,old_video;

    public Media_Video_Model(String video,String old_video) {
        this.video = video;
        this.old_video = old_video;
    }

    public String getOld_video() {
        return old_video;
    }

    public void setOld_video(String old_video) {
        this.old_video = old_video;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
