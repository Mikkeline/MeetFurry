package com.example.meetfurryapplication;


public class PetsClass {
    String PetName, PetType, PetBreed, PetGender, PetColor, PetAge,PetIntakeDate, PetDesc, UID, address, shelterName, filter;

    public PetsClass() { }



    public PetsClass(String petName, String petType, String petBreed, String petGender, String petColor, String petAge, String petIntakeDate, String petDesc, String UID, String address, String shelterName, String filter) {
        this.PetName = petName;
        this.PetType = petType;
        this.PetBreed = petBreed;
        this.PetGender = petGender;
        this.PetColor = petColor;
        this.PetAge = petAge;
        this.PetIntakeDate = petIntakeDate;
        this.PetDesc = petDesc;
        this.UID = UID;
        this.address = address;
        this.shelterName = shelterName;
        this.filter = filter;
    }

    public String getPetName() {
        return PetName;
    }

    public void setPetName(String petName) {
        PetName = petName;
    }

    public String getPetType() {
        return PetType;
    }

    public void setPetType(String petType) {
        PetType = petType;
    }

    public String getPetBreed() {
        return PetBreed;
    }

    public void setPetBreed(String petBreed) {
        PetBreed = petBreed;
    }

    public String getPetGender() {
        return PetGender;
    }

    public void setPetGender(String petGender) {
        PetGender = petGender;
    }

    public String getPetColor() {
        return PetColor;
    }

    public void setPetColor(String petColor) {
        PetColor = petColor;
    }

    public String getPetAge() {
        return PetAge;
    }

    public void setPetAge(String petAge) {
        PetAge = petAge;
    }

    public String getPetIntakeDate() {
        return PetIntakeDate;
    }

    public void setPetIntakeDate(String petIntakeDate) {
        PetIntakeDate = petIntakeDate;
    }

    public String getPetDesc() {
        return PetDesc;
    }

    public void setPetDesc(String petDesc) {
        PetDesc = petDesc;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}