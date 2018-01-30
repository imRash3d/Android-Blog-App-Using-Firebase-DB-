package com.explore.nmerp.ictblog;

/**
 * Created by NMERP on 18-Nov-17.
 */

public class Comment {

    private String post_key;
    private String commnent_key;
    private String user_id;
    private String time;
    private String username;
    private String img;
    private String comment;

    public Comment() {

    }

    public Comment(String user_id, String time, String username, String img, String comment) {
        this.user_id = user_id;
        this.time = time;
        this.username = username;
        this.img = img;
        this.comment = comment;

    }





    public String getId() {
        return user_id;
    }

    public void setId(String id) {
      this.user_id=id;
    }

    public String getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    public String getImg() {
        return img;
    }

    public String getComment() {
        return comment;
    }

public  String setPostKey(String post_key) {
    this.post_key=post_key;
    return this.post_key;

}

    public  String  gePostKey() {
        return post_key;

    }



    public void setCommnent_key(String commnent_key) {
        this.commnent_key = commnent_key;
    }
    public String getCommnent_key() {
        return commnent_key;
    }

}
