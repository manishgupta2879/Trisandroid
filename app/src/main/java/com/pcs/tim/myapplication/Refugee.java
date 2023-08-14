package com.pcs.tim.myapplication;

import java.io.Serializable;

/**
 * Created by Tim on 6/22/2017.
 */

public class Refugee implements Serializable{

    private String name;
    private String myRC;
    private String unhcrId;
    private String photoString = "";
    private byte[] photo;
    private double faceRecogResult;
    private String country;
    private String category;

    public double getFaceRecogResult(){return faceRecogResult;}

    public String getName() {
        return name;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public String getMyRC() {
        return myRC;
    }

    public String getUnhcrId() {
        return unhcrId;
    }

    public String getPhotoString() {
        return photoString;
    }

    public String getCountry() {
        return country;
    }

    public String getCategory(){return category; }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnhcrId(String unhcrId) {
        this.unhcrId = unhcrId;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Refugee(String name, String myRC, byte[] photo, String country, String category){
        if(name.isEmpty()){
            name = "";
        }
        if(country.isEmpty()){
            country ="";
        }
        if(category.isEmpty()){
            category = "";
        }
        this.name = name;
        this.myRC = myRC;
        this.photo = photo;
        this.country = country;
        this.category = category;
    }

    public Refugee(String name, String myRC, String unhcrId, byte[] photo, double faceRecogResult, String country, String category){
        this.name = name;
        this.myRC = myRC;
        this.unhcrId = unhcrId;
        this.photo = photo;
        this.faceRecogResult = faceRecogResult;
        this.country = country;
        this.category = category;
    }

    public Refugee(String myRC, double faceRecogResult){
        this.myRC = myRC;
        this.faceRecogResult = faceRecogResult;
    }

}
