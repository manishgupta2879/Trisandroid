package com.pcs.tim.myapplication;

public class TrackLogModel {
    private Long RegId;
    private String TrackType;
    private Long EnforcementId;
    private String Location;
    private Float Lat;
    private Float Lng;
    private String Remark;

    public Long getRegId() {
        return RegId;
    }

    public void setRegId(Long regId) {
        RegId = regId;
    }

    public String getTrackType() {
        return TrackType;
    }

    public void setTrackType(String trackType) {
        TrackType = trackType;
    }

    public Long getEnforcementId() {
        return EnforcementId;
    }

    public void setEnforcementId(Long enforcementId) {
        EnforcementId = enforcementId;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Float getLat() {
        return Lat;
    }

    public void setLat(Float lat) {
        Lat = lat;
    }

    public Float getLng() {
        return Lng;
    }

    public void setLng(Float lng) {
        Lng = lng;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public TrackLogModel(Long RegId, String TrackType, Long EnforcementId, String Location, Float Lat, Float Lng, String Remark){
        this.RegId = RegId;
        this.TrackType = TrackType;
        this.EnforcementId = EnforcementId;
        this.Location = Location;
        this.Lat = Lat;
        this.Lng = Lng;
        this.Remark = Remark;
    }
}
