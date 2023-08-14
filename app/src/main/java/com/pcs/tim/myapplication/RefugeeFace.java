package com.pcs.tim.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PCS on 20/4/2018.
 */

public class RefugeeFace implements Serializable {

    String mImagePath;
    double similarity;
    String inputType;
    String inputValue;
    ArrayList<Refugee> refugeeArrayList;

    public RefugeeFace(String mImagePath, double similarity, String inputType, String inputValue) {
        this.mImagePath = mImagePath;
        this.similarity = similarity;
        this.inputType = inputType;
        this.inputValue = inputValue;
    }

    public RefugeeFace(String mImagePath, ArrayList<Refugee> refugeeArrayList) {
        this.mImagePath = mImagePath;
        this.refugeeArrayList = refugeeArrayList;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public double getSimilarity() {
        return similarity;
    }

    public String getInputType() {
        return inputType;
    }

    public String getInputValue() {
        return inputValue;
    }

    public ArrayList<Refugee> getRefugeeArrayList() {
        return refugeeArrayList;
    }

}
