package com.pcs.tim.myapplication;

public class RemoteConfigResponse {
    private String Url;
    private String ApiUrl;
    private String AndroidVersion;
    private String IosVersion;
    private String AndroidPackageName;
    private String IosPackageName;

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getApiUrl() {
        return ApiUrl;
    }

    public void setApiUrl(String apiUrl) {
        ApiUrl = apiUrl;
    }

    public String getAndroidVersion() {
        return AndroidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        AndroidVersion = androidVersion;
    }

    public String getIosVersion() {
        return IosVersion;
    }

    public void setIosVersion(String iosVersion) {
        IosVersion = iosVersion;
    }

    public String getAndroidPackageName() {
        return AndroidPackageName;
    }

    public void setAndroidPackageName(String androidPackageName) {
        AndroidPackageName = androidPackageName;
    }

    public String getIosPackageName() {
        return IosPackageName;
    }

    public void setIosPackageName(String iosPackageName) {
        IosPackageName = iosPackageName;
    }
}
