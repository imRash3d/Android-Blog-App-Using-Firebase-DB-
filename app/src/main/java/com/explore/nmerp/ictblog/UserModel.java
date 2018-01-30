package com.explore.nmerp.ictblog;

/**
 * Created by NMERP on 13-Nov-17.
 */

public class UserModel {

    private String id;
    private String name;
    private String image;

    public UserModel(String name, String image) {

        this.name = name;
        this.image = image;
    }



    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }



}
