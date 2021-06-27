package com.example.meetfurryapplication.Model;

import java.util.Date;

public class Post extends PostId{

    private String forumImage,user,caption,Username;
    private Date time;

    public Post(){

    }

    public String getForumImage() {
        return forumImage;
    }

    public String getUser() {
        return user;
    }

    public String getUsername() { return Username; }

    public String getCaption() {
        return caption;
    }

    public Date getTime() {
        return time;
    }
}
