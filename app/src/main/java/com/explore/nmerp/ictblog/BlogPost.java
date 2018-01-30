package com.explore.nmerp.ictblog;

/**
 * Created by NMERP on 13-Nov-17.
 */

public class BlogPost {
    private String user_id;
    private String title;
    private String post;
    private String post_img;
    private String user_name;
    private String time;
    private String post_id;


    public BlogPost() {

    }

    public BlogPost(String title, String post, String post_img,String user_id,String user_name, String time,String post_id) {
        this.title = title;
        this.post = post;
        this.post_img = post_img;
        this.user_id=user_id;
        this.user_name=user_name;
        this.time=time;
        this.post_id=post_id;
    }


    public String getTitle() {
        return title;
    }

    public String getPost() {
        return post;
    }

    public String getPost_img() {
        return post_img;
    }

    public String getUser_Id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }



    public String getTime() {
        return time;
    }

    public String getPost_id() {
        return post_id;
    }
}
