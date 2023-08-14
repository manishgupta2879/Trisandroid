package com.pcs.tim.myapplication;

public class ViewDetailsModel {
    private String SearchOption;
    private String Data;


    public String getSearchOption() {
        return SearchOption;
    }

    public void setSearchOption(String searchOption) {
        SearchOption = searchOption;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public ViewDetailsModel(String searchOption, String data){
        this.SearchOption = searchOption;
        this.Data = data;
    }
}
