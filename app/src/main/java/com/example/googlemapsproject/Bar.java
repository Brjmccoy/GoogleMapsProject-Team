package com.example.googlemapsproject;

public class Bar {

    private String barName;
    private String address;
    private String barCode;
    private String website;
    private String genre;

    private String mondayHappyHours;
    private String tuesdayHappyHours;
    private String wednesdayHappyHours;
    private String thursdayHappyHours;
    private String fridayHappyHours;
    private String saturdayHappyHours;
    private String sundayHappyHours;

    public Bar() {
        barName = "";
        address = "";
        barCode = "";
        website = "";
        genre = "";

        mondayHappyHours = "";
        tuesdayHappyHours = "";
        wednesdayHappyHours = "";
        thursdayHappyHours = "";
        fridayHappyHours = "";
        saturdayHappyHours = "";
        sundayHappyHours = "";
    }



    public String getBarCode() {
        return barCode;
    }

    public String getAddress() {
        return address;
    }

    public String getBarName() {
        return barName;
    }

    public String getMondayHappyHours() {
        return mondayHappyHours;
    }

    public String getTuesdayHappyHours() {
        return tuesdayHappyHours;
    }

    public String getWebsite() {
        return website;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFridayHappyHours() {
        return fridayHappyHours;
    }

    public String getWednesdayHappyHours() {
        return wednesdayHappyHours;
    }

    public String getSaturdayHappyHours() {
        return saturdayHappyHours;
    }

    public String getThursdayHappyHours() {
        return thursdayHappyHours;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public void setMondayHappyHours(String mondayHappyHours) {
        this.mondayHappyHours = mondayHappyHours;
    }

    public void setTuesdayHappyHours(String tuesdayHappyHours) {
        this.tuesdayHappyHours = tuesdayHappyHours;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setWednesdayHappyHours(String wednesdayHappyHours) {
        this.wednesdayHappyHours = wednesdayHappyHours;
    }

    public String getSundayHappyHours() {
        return sundayHappyHours;
    }

    public void setFridayHappyHours(String fridayHappyHours) {
        this.fridayHappyHours = fridayHappyHours;
    }

    public void setSaturdayHappyHours(String saturdayHappyHours) {
        this.saturdayHappyHours = saturdayHappyHours;
    }

    public void setSundayHappyHours(String sundayHappyHours) {
        this.sundayHappyHours = sundayHappyHours;
    }

    public void setThursdayHappyHours(String thursdayHappyHours) {
        this.thursdayHappyHours = thursdayHappyHours;
    }

    @Override
    public String toString() {
        return "Bar Code: " + barCode + ", Bar Name: " + barName + ", Address: " + address +
                ", Bar Website: " + website + ", \n Monday Happy Hours: " + mondayHappyHours
                + ", \n Tuesday Happy Hours: " + tuesdayHappyHours
                + ", \n Wednesday Happy Hours: " + wednesdayHappyHours
                + ", \n Thursday Happy Hours: " + thursdayHappyHours
                + ", \n Friday Happy Hours: " + fridayHappyHours
                + ", \n Saturday Happy Hours: " + saturdayHappyHours
                + ", \n Sunday Happy Hours: " + sundayHappyHours;
    }


}
