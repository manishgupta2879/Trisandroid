package com.pcs.tim.myapplication;

import java.io.Serializable;

public class ViewRefugee implements Serializable {

    private String name;
    private String myRC;
    private String unhcrId;
    private String photoString;
    private String photo;
    private double faceRecogResult;
    private String country;
    private String category;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMyRC() {
        return myRC;
    }

    public void setMyRC(String myRC) {
        this.myRC = myRC;
    }

    public String getUnhcrId() {
        return unhcrId;
    }

    public void setUnhcrId(String unhcrId) {
        this.unhcrId = unhcrId;
    }

    public String getPhotoString() {
        return photoString;
    }

    public void setPhotoString(String photoString) {
        this.photoString = photoString;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getFaceRecogResult() {
        return faceRecogResult;
    }

    public void setFaceRecogResult(double faceRecogResult) {
        this.faceRecogResult = faceRecogResult;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ViewRefugee(String fullname, String myrc, String photo, String country, String category){
        this.name = fullname;
        this.myRC = myrc;
        this.photo = photo;
        this.country = country;
        this.category = category;
    }
}
