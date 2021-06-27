package com.example.meetfurryapplication;

public class UsersClass {
    String Name;
    String Email;
    String Contact;
    String Address;
    public String username;
    String password;
    String userType;
    String uid;
    String imageUrl;


    public UsersClass(){

    }

    public UsersClass(String Name, String Email, String Contact, String Address, String username, String password, String userType, String uid){
        this.Name = Name;
        this.Email = Email;
        this.Contact = Contact;
        this.Address = Address;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.uid = uid;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
