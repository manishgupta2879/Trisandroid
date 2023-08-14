package com.pcs.tim.myapplication;

public class ViewVaccineModel {
    private String Myrc;
    private String PhoneNo;

    public String getMyrc() {
        return Myrc;
    }

    public void setMyrc(String myrc) {
        Myrc = myrc;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public ViewVaccineModel(String myrc, String phoneno){
        this.PhoneNo  = phoneno;
        this.Myrc = myrc;
    }
}
