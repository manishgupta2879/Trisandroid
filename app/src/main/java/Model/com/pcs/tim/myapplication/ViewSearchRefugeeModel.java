package com.pcs.tim.myapplication;

public class ViewSearchRefugeeModel {
    private String MyrcOrName;
    private String UnhcrId;
    private String CountryOfOrigin;
    private String State;
    private String Category;



    public ViewSearchRefugeeModel(String myrcorname,String unhcrid,String countryoforigin, String state, String category){
        this.setMyrcOrName(myrcorname);
        this.setUnhcrId(unhcrid);
        this.setCountryOfOrigin(countryoforigin);
        this.setState(state);
        this.setCategory(category);
    }

    public String getMyrcOrName() {
        return MyrcOrName;
    }

    public void setMyrcOrName(String myrcOrName) {
        MyrcOrName = myrcOrName;
    }

    public String getUnhcrId() {
        return UnhcrId;
    }

    public void setUnhcrId(String unhcrId) {
        UnhcrId = unhcrId;
    }

    public String getCountryOfOrigin() {
        return CountryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        CountryOfOrigin = countryOfOrigin;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
