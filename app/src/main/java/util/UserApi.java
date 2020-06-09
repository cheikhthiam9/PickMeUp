package util;

import android.app.Application;
import android.net.Uri;

public class UserApi extends Application {

    private String firstName;
    private String lastName;
    private String userIdDatabase; //
    private String accountNumber; //
    private String email;
    private String passwordUser;
    private String phoneNumberUser;
    private String streetAddressUser;
    private String cityUser;
    private String zipCodeUser;
    private String stateUser;
    private String countryUser;
    private String status; //
    private String documentId; //
    private String documentUpload;
    private String profilPic; //
    private String membershipId; //

    public UserApi(String streetAddressUser, String cityUser, String zipCodeUser, String stateUser, String countryUser) {
        this.streetAddressUser = streetAddressUser;
        this.cityUser = cityUser;
        this.zipCodeUser = zipCodeUser;
        this.stateUser = stateUser;
        this.countryUser = countryUser;
    }

    public String getDocumentUpload() {
        return documentUpload;
    }

    public void setDocumentUpload(String documentUpload) {
        this.documentUpload = documentUpload;
    }

    public void setUserIdDatabase(String userIdDatabase) {
        this.userIdDatabase = userIdDatabase;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }



    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreetAddressUser() {
        return streetAddressUser;
    }

    public void setStreetAddressUser(String streetAddressUser) {
        this.streetAddressUser = streetAddressUser;
    }

    public String getCityUser() {
        return cityUser;
    }

    public void setCityUser(String cityUser) {
        this.cityUser = cityUser;
    }

    public String getZipCodeUser() {
        return zipCodeUser;
    }

    public void setZipCodeUser(String zipCodeUser) {
        this.zipCodeUser = zipCodeUser;
    }

    public String getStateUser() {
        return stateUser;
    }

    public void setStateUser(String stateUser) {
        this.stateUser = stateUser;
    }

    public String getCountryUser() {
        return countryUser;
    }

    public void setCountryUser(String countryUser) {
        this.countryUser = countryUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getPhoneNumberUser() {
        return phoneNumberUser;
    }

    public void setPhoneNumberUser(String phoneNumberUser) {
        this.phoneNumberUser = phoneNumberUser;
    }

    public String getProfilPic() {
        return profilPic;
    }

    public void setProfilPic(String profilPic) {
        this.profilPic = profilPic;
    }


    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public static void setInstance(UserApi instance) {
        UserApi.instance = instance;
    }

    private static UserApi instance;

    public static UserApi getInstance() {
        if (instance == null) {
            instance = new UserApi();
        }
        return instance;
    }

    public UserApi(){}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserIdDatabase() {
        return userIdDatabase;
    }

}
