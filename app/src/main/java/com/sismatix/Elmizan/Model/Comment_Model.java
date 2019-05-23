package com.sismatix.Elmizan.Model;

public class Comment_Model {
    String name,id,time,comment;

    public Comment_Model(String name, String id, String time, String comment) {
        this.name = name;
        this.id = id;
        this.time = time;
        this.comment = comment;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
