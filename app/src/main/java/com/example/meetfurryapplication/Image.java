package com.example.meetfurryapplication;

import android.net.Uri;

public class Image {

    private String imageUrl, petAge, petBreed, petColor, petDesc, petGender, petIntakeDate, petName, petType,address;

    public Image() {}

    public Image(String imagename, String image) {
        this.petName = imagename;
        this.imageUrl = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPetAge() {
        return petAge;
    }

    public String getAddress() {
        return address;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public String getPetColor() {
        return petColor;
    }

    public String getPetDesc() {
        return petDesc;
    }

    public String getPetGender() {
        return petGender;
    }

    public String getPetIntakeDate() {
        return petIntakeDate;
    }

    public String getPetName() {
        return petName;
    }

    public String getPetType() {
        return petType;
    }
}
