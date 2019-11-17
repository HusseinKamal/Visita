package com.emergency.app.models;

public class Request {
    public Request(int requestId,String requestType,String requestAddress, String guestType, String requestDescription, String report, String dateTime, String status, String employeeMobile, String employeeJob, String employeeName, String guestName, String employeePhoto, String guestPhoto, int guestID, int employeeID, String price) {
        this.requestType=requestType;
        this.requestAddress = requestAddress;
        this.guestType = guestType;
        this.requestDescription = requestDescription;
        this.report = report;
        this.dateTime = dateTime;
        this.status = status;
        this.employeeMobile = employeeMobile;
        this.employeeJob = employeeJob;
        this.employeeName = employeeName;
        this.guestName = guestName;
        this.employeePhoto = employeePhoto;
        this.guestPhoto = guestPhoto;
        this.requestId = requestId;
        this.guestID = guestID;
        this.employeeID = employeeID;
        this.price = price;
    }

    public String getRequestAddress() {
        return requestAddress;
    }

    public void setRequestAddress(String requestAddress) {
        this.requestAddress = requestAddress;
    }

    public String getGuestType() {
        return guestType;
    }

    public void setGuestType(String guestType) {
        this.guestType = guestType;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeMobile() {
        return employeeMobile;
    }

    public void setEmployeeMobile(String employeeMobile) {
        this.employeeMobile = employeeMobile;
    }

    public String getEmployeeJob() {
        return employeeJob;
    }

    public void setEmployeeJob(String employeeJob) {
        this.employeeJob = employeeJob;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getEmployeePhoto() {
        return employeePhoto;
    }

    public void setEmployeePhoto(String employeePhoto) {
        this.employeePhoto = employeePhoto;
    }

    public String getGuestPhoto() {
        return guestPhoto;
    }

    public void setGuestPhoto(String guestPhoto) {
        this.guestPhoto = guestPhoto;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getGuestID() {
        return guestID;
    }

    public void setGuestID(int guestID) {
        this.guestID = guestID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String requestAddress;
    private String guestType;
    private String requestDescription;
    private String report;
    private String dateTime;
    private String status;
    private String employeeMobile;
    private String employeeJob;
    private String employeeName;
    private String guestName;
    private String employeePhoto;
    private String guestPhoto;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    private String requestType,price;
    private int requestId,guestID,employeeID;
}
