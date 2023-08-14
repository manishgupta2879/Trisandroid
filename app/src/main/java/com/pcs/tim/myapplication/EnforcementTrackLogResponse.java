package com.pcs.tim.myapplication;

public class EnforcementTrackLogResponse {
    private Long regId;
    private String myrc;
    private String fullName;
    private String countryOfOrigin;
    private String cardExpiredDate;
    private String registerDate;
    private String cardStatus;
    private Boolean isBlackList;
    private String photoURL;
    private String trackType;
    private Long enforcementId;
    private String location;
    private Double lat;
    private Double lng;
    private String remark;
    private String createdTime;

    public Long getRegId() {
        return regId;
    }

    public void setRegId(Long regId) {
        this.regId = regId;
    }

    public String getMyrc() {
        return myrc;
    }

    public void setMyrc(String myrc) {
        this.myrc = myrc;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public Boolean getBlackList() {
        return isBlackList;
    }

    public void setBlackList(Boolean blackList) {
        isBlackList = blackList;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getTrackType() {
        return trackType;
    }

    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }

    public Long getEnforcementId() {
        return enforcementId;
    }

    public void setEnforcementId(Long enforcementId) {
        this.enforcementId = enforcementId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
