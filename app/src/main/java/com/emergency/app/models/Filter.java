package com.emergency.app.models;

import java.io.Serializable;

public class Filter implements Serializable {
    public Filter()
    {

    }

    public Filter(String name, String location, String job, int price) {
        this.name = name;
        this.location = location;
        this.job = job;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private String name,location,job;
    private int price;
}
