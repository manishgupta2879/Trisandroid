package com.pcs.tim.myapplication;

public class NewRegisterModel {
    private String PoliceId;
    private String IcNumber;
    private String Fullname;
    private String MobileNo;
    private String PoliceRank;
    private String Agency;
    private String State;
    private String Station;
    private String Department;
    private String Password;
    private String Photo;

    public String getPoliceId() {
        return PoliceId;
    }

    public void setPoliceId(String policeId) {
        PoliceId = policeId;
    }

    public String getIcNumber() {
        return IcNumber;
    }

    public void setIcNumber(String icNumber) {
        IcNumber = icNumber;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getPoliceRank() {
        return PoliceRank;
    }

    public void setPoliceRank(String policeRank) {
        PoliceRank = policeRank;
    }

    public String getAgency() {
        return Agency;
    }

    public void setAgency(String agency) {
        Agency = agency;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getStation() {
        return Station;
    }

    public void setStation(String station) {
        Station = station;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public NewRegisterModel(String policeid,String icnumber,String fullname, String mobileno, String policerank, String agency,
                            String state,String station,String department,String password,String photo){
        this.PoliceId = policeid;
        this.IcNumber = icnumber;
        this.Fullname = fullname;
        this.MobileNo = mobileno;
        this.PoliceRank = policerank;
        this.Agency = agency;
        this.State = state;
        this.Station = station;
        this.Department = department;
        this.Password = password;
        this.Photo = photo;
    }
}
