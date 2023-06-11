package com.oneforall.config;

import java.util.Arrays;

public class UserDetailsModel {

    public UserDetailsModel getUserByName(String userName) {
        return Arrays.stream(userDetailsModels).filter(userDetailsModel -> userDetailsModel.getName().equalsIgnoreCase(userName)).findFirst().get();
    }

    public UserDetailsModel getTestData(int index) {
        return userDetailsModels[index];
    }
    private String name;
    private String mobileNumber;
    private String otp;
    private String wrongOTP;
    private String gender;
    private String age;
    private String email;
    private String password;
    public String getName() {
        return name;
    }
    public String getMobileNumber() {
        return mobileNumber;
    }
    public String getOtp() {
        return otp;
    }
    public String getWrongOTP() {
        return wrongOTP;
    }
    public String getGender() {
        return gender;
    }
    public String getAge() {
        return age;
    }
    private UserDetailsModel[] userDetailsModels;
    public String getEmail(){return email;}
    public String getPassword(){return password;}

    public UserDetailsModel(UserDetailsModel[] userDetailsModels) {
        this.userDetailsModels = userDetailsModels;
    }
    public UserDetailsModel() {
    }


}
