package com.emergency.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Chat implements Serializable {
    public Chat()
    {

    }
    public Chat(String id, String sender_name, String photo, String message, String count, String time,String senderId) {
        this.id = id;
        this.sender_name = sender_name;
        this.photo = photo;
        this.message = message;
        this.count = count;
        this.time = time;
        this.senderId=senderId;
    }

    public ArrayList<Chat> getData() {
        return data;
    }

    public void setData(ArrayList<Chat> data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @SerializedName("data")
    @Expose
    private ArrayList<Chat> data;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("sender_name")
    @Expose
    private String sender_name;

    @SerializedName("photo")
    @Expose
    private String photo;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("count")
    @Expose
    private String count;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("senderId")
    @Expose
    private String senderId;

    @SerializedName("admins ")
    @Expose
    private ArrayList<User> admins;


    @SerializedName("students")
    @Expose
    private ArrayList<User> students;

    public ArrayList<User> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<User> admins) {
        this.admins = admins;
    }

    public ArrayList<User> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<User> students) {
        this.students = students;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }


}
