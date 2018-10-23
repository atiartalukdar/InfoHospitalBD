package com.infohospital.infohospitalbd;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ProfileDataModel {

    public String name;
    public String email;
    public String phone;
    public String address;
    public String password;
    public String profileImageLink;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public ProfileDataModel() {
    }

    public ProfileDataModel(String name, String email, String phone, String address, String password, String profileImageLink) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.profileImageLink = profileImageLink;
    }

}


