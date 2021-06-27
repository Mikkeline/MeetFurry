package com.example.meetfurryapplication;

public class ChatLists {

    public String name,image;

    public ChatLists()
    {

    }

    public ChatLists(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
