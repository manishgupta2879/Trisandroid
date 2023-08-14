package com.pcs.tim.myapplication;

public class PartialViewTrackLogs {
    private Long regId;
    private String trackType;
    private Long enforcementId;
    private String location;
    private Float lat;
    private Float lng;
    private String remark;
    private String createdTime;

    public Long getRegId() {
        return regId;
    }

    public void setRegId(Long regId) {
        this.regId = regId;
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

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
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
