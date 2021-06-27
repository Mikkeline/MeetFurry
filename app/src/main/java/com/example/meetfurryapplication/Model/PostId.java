package com.example.meetfurryapplication.Model;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class PostId {

@Exclude
    public String PostId;

public <T extends PostId> T withId(@NonNull final String id){

    this.PostId = id;
    return (T) this;
}

}
