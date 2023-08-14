package com.pcs.tim.myapplication;

public class RequestTokenModel {
    private Integer moduleId;
    private String apiKey;

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public RequestTokenModel(Integer modID, String apiKey){
        this.moduleId = modID;
        this.apiKey = apiKey;
    }
}
