package com.pcs.tim.myapplication;

import java.util.List;

public class ViewTrackLogResponse {
    private Long regId;
    private String myrc;
    private String fullName;
    private String countryOfOrigin;
    private String cardExpiredDate;
    private String cardStatus;
    private Boolean isBlackList;
    private String photoURL;

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

}
