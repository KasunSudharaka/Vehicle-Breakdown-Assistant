package com.example.loginapp;

public class HelpInformation {

    public String voname;
    public String vocontact;
    public String description;


    public HelpInformation(){


    }

    public HelpInformation(String voname,String vocontact, String description){

        this.voname=voname;
        this.vocontact=vocontact;
        this.description=description;
    }



    public String getVoname() {
        return voname;
    }

    public void setVoname(String voname) {
        this.voname = voname;
    }

    public String getVocontact() {
        return vocontact;
    }

    public void setVocontact(String vocontact) {
        this.vocontact = vocontact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
