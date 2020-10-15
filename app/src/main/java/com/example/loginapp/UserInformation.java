package com.example.loginapp;

public class UserInformation {

    public String fullname;
    public String id;
    public String mobile;
    public String city;
    public String district;
    public String address;
    public String ssname;
    public String contactnumber;
    public String serviceemail;
    public String openhours;


    public UserInformation(){

    }

    public UserInformation(String fullname, String id, String mobile, String city, String district, String address, String ssname, String contactnumber,String serviceemail,String openhours) {
        this.fullname = fullname;
        this.id = id;
        this.mobile = mobile;
        this.city = city;
        this.district = district;
        this.address = address;
        this.ssname = ssname;
        this.contactnumber = contactnumber;
        this.serviceemail=serviceemail;
        this.openhours=openhours;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSsname() {
        return ssname;
    }

    public void setSsname(String ssname) {
        this.ssname = ssname;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }


    public String getServiceemail() {
        return serviceemail;
    }

    public void setServiceemail(String serviceemail) {
        this.serviceemail = serviceemail;
    }

    public String getOpenhours() {
        return openhours;
    }

    public void setOpenhours(String openhours) {
        this.openhours = openhours;
    }



}


