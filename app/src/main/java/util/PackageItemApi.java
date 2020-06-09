package util;

import android.app.Application;

import java.util.ArrayList;

public class PackageItemApi extends Application {



    private String userAccountNumber;
    private String userFullName;
    private String packageId;
    private String packageDescription;
    private String postalServiceProvider;
    private String onlineShopProvider;
    private String receivedDate;
    private String packageCondition;
    private String packageLength;
    private String packageWidth;
    private String packageHeight;
    private String packageWeight;
    private ArrayList<String> selectPackageList;

    public ArrayList<String> getSelectPackageList() {
        return selectPackageList;
    }

    public void setSelectPackageList(ArrayList<String> selectPackageList) {
        this.selectPackageList = selectPackageList;
    }

    public static void setInstance(PackageItemApi instance) {
        PackageItemApi.instance = instance;
    }

    private static PackageItemApi instance;

    public static PackageItemApi getInstance() {
        if (instance == null) {
            instance = new PackageItemApi();
        }
        return instance;
    }


    public PackageItemApi(String packageId, String packageDescription, String packageLength, String packageWidth, String packageHeight, String packageWeight) {
        this.packageId = packageId;
        this.packageDescription = packageDescription;
        this.packageLength = packageLength;
        this.packageWidth = packageWidth;
        this.packageHeight = packageHeight;
        this.packageWeight = packageWeight;
    }

    public PackageItemApi(String userAccountNumber, String userFullName, String packageId, String packageDescription, String postalServiceProvider, String onlineShopProvider, String receivedDate, String packageCondition, String packageLength, String packageWidth, String packageHeight, String packageWeight) {
        this.userAccountNumber = userAccountNumber;
        this.userFullName = userFullName;
        this.packageId = packageId;
        this.packageDescription = packageDescription;
        this.postalServiceProvider = postalServiceProvider;
        this.onlineShopProvider = onlineShopProvider;
        this.receivedDate = receivedDate;
        this.packageCondition = packageCondition;
        this.packageLength = packageLength;
        this.packageWidth = packageWidth;
        this.packageHeight = packageHeight;
        this.packageWeight = packageWeight;
    }

    public PackageItemApi(String packageId, String packageDescription, String postalServiceProvider, String onlineShopProvider, String receivedDate, String packageCondition, String packageLength, String packageWidth, String packageHeight, String packageWeight) {
        this.packageId = packageId;
        this.packageDescription = packageDescription;
        this.postalServiceProvider = postalServiceProvider;
        this.onlineShopProvider = onlineShopProvider;
        this.receivedDate = receivedDate;
        this.packageCondition = packageCondition;
        this.packageLength = packageLength;
        this.packageWidth = packageWidth;
        this.packageHeight = packageHeight;
        this.packageWeight = packageWeight;
    }

    public PackageItemApi() {
    }

    public String getUserAccountNumber() {
        return userAccountNumber;
    }

    public void setUserAccountNumber(String userAccountNumber) {
        this.userAccountNumber = userAccountNumber;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public String getPostalServiceProvider() {
        return postalServiceProvider;
    }

    public void setPostalServiceProvider(String postalServiceProvider) {
        this.postalServiceProvider = postalServiceProvider;
    }

    public String getOnlineShopProvider() {
        return onlineShopProvider;
    }

    public void setOnlineShopProvider(String onlineShopProvider) {
        this.onlineShopProvider = onlineShopProvider;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getPackageCondition() {
        return packageCondition;
    }

    public void setPackageCondition(String packageCondition) {
        this.packageCondition = packageCondition;
    }

    public String getPackageLength() {
        return packageLength;
    }

    public void setPackageLength(String packageLength) {
        this.packageLength = packageLength;
    }

    public String getPackageWidth() {
        return packageWidth;
    }

    public void setPackageWidth(String packageWidth) {
        this.packageWidth = packageWidth;
    }

    public String getPackageHeight() {
        return packageHeight;
    }

    public void setPackageHeight(String packageHeight) {
        this.packageHeight = packageHeight;
    }

    public String getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(String packageWeight) {
        this.packageWeight = packageWeight;
    }
}
