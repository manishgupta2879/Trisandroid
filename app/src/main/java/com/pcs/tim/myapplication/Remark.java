package com.pcs.tim.myapplication;

/**
 * Created by Tim on 6/30/2017.
 */

public class Remark {

    private String policeId;
    private String remark;
    private String myRc;
    private String location;
    private String checkTime;
    private String refugeeName;
    private String photo;
    private String countryOfOrigin;
    private String cardExpiredDate;

    public long getRegId() {
        return regId;
    }

    public void setRegId(long regId) {
        this.regId = regId;
    }

    private float lat;
    private long regId;
    private float lng;
    private String cardStatus;

    Remark(String policeId, String remark, String myRc, String location, String checkTime, String refugeeName,
           String photo, String countryOfOrigin, String cardExpiredDate, String cardStatus,float lat,float lng,long regId) {
        this.setPoliceId(policeId);
        this.setRemark(remark);
        this.setMyRc(myRc);
        this.setLocation(location);
        this.setCheckTime(checkTime);
        this.setRefugeeName(refugeeName);
        this.setPhoto(photo);
        this.setCountryOfOrigin(countryOfOrigin);
        this.setCardExpiredDate(cardExpiredDate);
        this.setCardStatus(cardStatus);
        this.setLat(lat);
        this.setLng(lng);
        this.setRegId(regId);

    }

    public String getPoliceId() {
        return policeId;
    }

    public void setPoliceId(String policeId) {
        this.policeId = policeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMyRc() {
        return myRc;
    }

    public void setMyRc(String myRc) {
        this.myRc = myRc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getRefugeeName() {
        return refugeeName;
    }

    public void setRefugeeName(String refugeeName) {
        this.refugeeName = refugeeName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getCardExpiredDate() {
        return cardExpiredDate;
    }

    public void setCardExpiredDate(String cardExpiredDate) {
        this.cardExpiredDate = cardExpiredDate;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }
}
