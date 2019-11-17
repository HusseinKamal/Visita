package com.emergency.app.models;

public class User {
    public User()
    {

    }
    public User(int id, String token, String name, String email, String mobile, String password, String price, String type, String job, String address, String photo, String device_token,int orders,int reviews,String rate,boolean isRecommend,int jobID,String location) {
        this.id = id;
        this.token = token;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.price = price;
        this.type = type;
        this.job = job;
        this.address = address;
        this.photo = photo;
        this.device_token = device_token;
        this.rate = rate;
        this.orders = orders;
        this.reviews = reviews;
        this.isRecommend = isRecommend;
        this.job = job;
        this.jobID=jobID;
        this.location=location;

    }
    public User(int id, String token, String name, String email, String mobile, String password, String type, String address, String photo, String device_token) {
        this.id = id;
        this.token = token;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.type = type;
        this.address = address;
        this.photo = photo;
        this.device_token = device_token;
    }

    public User(int id, String name,String job, String type, String photo, int orders, int reviews, String rate,String price) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.orders = orders;
        this.reviews = reviews;
        this.rate = rate;
        this.type = type;
        this.photo = photo;
        this.price=price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public boolean isRecommend() {
        return isRecommend;
    }

    public void setRecommend(boolean recommend) {
        isRecommend = recommend;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    private int id,jobID,orders,reviews;
    private boolean isRecommend;
    private String token,name,email,mobile,password,price,type,job,address,photo,device_token,rate,location;
}
