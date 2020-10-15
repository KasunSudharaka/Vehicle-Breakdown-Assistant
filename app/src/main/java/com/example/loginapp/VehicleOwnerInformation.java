package com.example.loginapp;

public class VehicleOwnerInformation {

    public String fullname;
    public String id;
    public String mobile;
    public String address;
    public String vehicletype;
    public String vehicle;
    public String regno;
    public String contactnumber;



    public VehicleOwnerInformation(){}

    public VehicleOwnerInformation(String fullname, String id, String mobile, String address, String vehicletype, String vehicle, String regno, String contactnumber) {
        this.fullname = fullname;
        this.id = id;
        this.mobile = mobile;
        this.address = address;
        this.vehicletype = vehicletype;
        this.vehicle = vehicle;
        this.regno = regno;
        this.contactnumber = contactnumber;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(String vehicletype) {
        this.vehicletype = vehicletype;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }
}
