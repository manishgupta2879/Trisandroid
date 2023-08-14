package com.pcs.tim.myapplication;

public class ChangePasswordModel {
    private Long PoliceId;
    private String Password;
    private String NewPassword;

    public Long getPoliceId() {
        return PoliceId;
    }

    public void setPoliceId(Long policeId) {
        PoliceId = policeId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public ChangePasswordModel(Long policeid, String password,String newpassword){
        this.PoliceId = policeid;
        this.Password = password;
        this.NewPassword = newpassword;
    }
}
