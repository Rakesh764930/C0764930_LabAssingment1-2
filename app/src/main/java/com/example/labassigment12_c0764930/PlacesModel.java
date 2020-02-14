package com.example.labassigment12_c0764930;


public class PlacesModel {

    int id ;
    String address;
    String name;
    String date;
    Double latitude;
    Double longitude;

    public PlacesModel(int id, String name,String date,String address,Double longitude,Double latitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getDate() {
        return date;
    }
}
