package com.pcs.tim.myapplication;

public class AppVersionModel {
    private  String name;
    private String platform;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public AppVersionModel(String name,String platform){
        this.name = name;
        this.platform = platform;
    }
}
