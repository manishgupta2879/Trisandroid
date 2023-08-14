package com.pcs.tim.myapplication;

public class UploadPhoto {
    private String type;
    private String  base64String;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }

    public UploadPhoto(String in_type,String in_base64String){
        this.type = in_type;
        this.base64String = in_base64String;
    }
}
