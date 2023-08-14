package com.pcs.tim.myapplication;

public class EnforcementLoginModel {
    private String PoliceId;
    private String Password;


    public String getPoliceId() {
        return PoliceId;
    }

    public void setPoliceId(String policeId) {
        PoliceId = policeId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public EnforcementLoginModel(String policeid, String password){
        this.PoliceId = policeid;
        this.Password = password;
    }
}
