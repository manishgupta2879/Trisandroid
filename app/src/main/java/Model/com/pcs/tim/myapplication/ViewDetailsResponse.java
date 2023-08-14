package com.pcs.tim.myapplication;

public class ViewDetailsResponse {
    private PartialViewProfile profile;
    private PartialViewHealthStatus healthStatus;
    private PartialViewEmployer employer;
    PartialViewCardStatus cardStatus;
    private String photo;

    // Getter Methods

    public PartialViewProfile getProfile() {
        return profile;
    }

    public PartialViewHealthStatus getHealthStatus() {
        return healthStatus;
    }

    public PartialViewEmployer getEmployer() {
        return employer;
    }

    public PartialViewCardStatus getCardStatus() {
        return cardStatus;
    }

    public String getPhoto() {
        return photo;
    }

    // Setter Methods

    public void setProfile(PartialViewProfile profile) {
        this.profile = profile;
    }

    public void setHealthStatus(PartialViewHealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public void setEmployer(PartialViewEmployer employer) {
        this.employer = employer;
    }

    public void setCardStatus(PartialViewCardStatus cardStatus) {
        this.cardStatus = cardStatus;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
