package com.pcs.tim.myapplication;

public class EnforcementUpdateModel {
    private Long id;
    private String policeId;
    private String icNumber;
    private String fullname;
    private String mobileNo;
    private String policeRank;
    private String department;
    private String agency;
    private String state;
    private String station;
    private String password;
    private String photo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoliceId() {
        return policeId;
    }

    public void setPoliceId(String policeId) {
        this.policeId = policeId;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPoliceRank() {
        return policeRank;
    }

    public void setPoliceRank(String policeRank) {
        this.policeRank = policeRank;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public EnforcementUpdateModel(Long id,String policeid,String icnumber,String fullname,String mobileno,String policerank,String agency,String state,
                                  String station,String department,String password,String photo){
        this.id = id;
        this.policeId = policeid;
        this.icNumber = icnumber;
        this.fullname = fullname;
        this.mobileNo = mobileno;
        this.policeRank = policerank;
        this.agency = agency;
        this.state = state;
        this.station = station;
        this.department = department;
        this.password = password;
        this.photo = photo;
    }
}
