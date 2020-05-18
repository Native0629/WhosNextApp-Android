package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DiscoverModel implements Serializable {

    @SerializedName("CustomerList")
    private ArrayList<CustomerList> customerList;

    public ArrayList<CustomerList> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(ArrayList<CustomerList> customerList) {
        this.customerList = customerList;
    }

    public static class CustomerList implements Serializable {
        @SerializedName("Website")
        private String Website;
        @SerializedName("UserName")
        private String UserName;
        @SerializedName("UserId")
        private String UserId;
        @SerializedName("TotalViews")
        private int TotalViews;
        @SerializedName("TotalProfileVideoViews")
        private int TotalProfileVideoViews;
        @SerializedName("TotalPost")
        private String TotalPost;
        @SerializedName("TotalFollowings")
        private String TotalFollowings;
        @SerializedName("TotalFollowers")
        private String TotalFollowers;
        @SerializedName("Timer")
        private int Timer;
        @SerializedName("SubscriptionStartDate")
        private String SubscriptionStartDate;
        @SerializedName("SubscriptionPrice")
        private int SubscriptionPrice;
        @SerializedName("SubscriptionId")
        private int SubscriptionId;
        @SerializedName("SubscriptionEndDate")
        private String SubscriptionEndDate;
        @SerializedName("ProfilePicUrl")
        private String ProfilePicUrl;
        @SerializedName("ProfilePicBase64String")
        private String ProfilePicBase64String;
        @SerializedName("Password")
        private String Password;
        @SerializedName("NotificationMessage")
        private String NotificationMessage;
        @SerializedName("LastName")
        private String LastName;
        @SerializedName("IsStar")
        private int IsStar;
        @SerializedName("IsSnippets")
        private boolean IsSnippets;
        @SerializedName("IsRequested")
        private boolean IsRequested;
        @SerializedName("IsPrivate")
        private boolean IsPrivate;
        @SerializedName("IsFollowing")
        private boolean IsFollowing;
        @SerializedName("IsDeactivate")
        private boolean IsDeactivate;
        @SerializedName("IsAdmin")
        private boolean IsAdmin;
        @SerializedName("InsertDateTime")
        private String InsertDateTime;
        @SerializedName("FollowerRequestId")
        private int FollowerRequestId;
        @SerializedName("FirstName")
        private String FirstName;
        @SerializedName("Email")
        private String Email;
        @SerializedName("DeviceType")
        private String DeviceType;
        @SerializedName("DeviceToken")
        private String DeviceToken;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("CloudVideoUrl")
        private String CloudVideoUrl;
        @SerializedName("City")
        private String City;
        @SerializedName("ChatId")
        private String ChatId;
        @SerializedName("CategoryNames")
        private String CategoryNames;
        @SerializedName("CategoryList")
        private String CategoryList;
        @SerializedName("CategoryIds")
        private String CategoryIds;
        @SerializedName("BioVideoUrl")
        private String BioVideoUrl;
        @SerializedName("BioVideoBase64String")
        private String BioVideoBase64String;
        @SerializedName("ActiveCustomerId")
        private int ActiveCustomerId;
        @SerializedName("AboutSelf")
        private String AboutSelf;

        public String getWebsite() {
            return Website;
        }

        public void setWebsite(String Website) {
            this.Website = Website;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public int getTotalViews() {
            return TotalViews;
        }

        public void setTotalViews(int TotalViews) {
            this.TotalViews = TotalViews;
        }

        public int getTotalProfileVideoViews() {
            return TotalProfileVideoViews;
        }

        public void setTotalProfileVideoViews(int TotalProfileVideoViews) {
            this.TotalProfileVideoViews = TotalProfileVideoViews;
        }

        public String getTotalPost() {
            return TotalPost;
        }

        public void setTotalPost(String TotalPost) {
            this.TotalPost = TotalPost;
        }

        public String getTotalFollowings() {
            return TotalFollowings;
        }

        public void setTotalFollowings(String TotalFollowings) {
            this.TotalFollowings = TotalFollowings;
        }

        public String getTotalFollowers() {
            return TotalFollowers;
        }

        public void setTotalFollowers(String TotalFollowers) {
            this.TotalFollowers = TotalFollowers;
        }

        public int getTimer() {
            return Timer;
        }

        public void setTimer(int Timer) {
            this.Timer = Timer;
        }

        public String getSubscriptionStartDate() {
            return SubscriptionStartDate;
        }

        public void setSubscriptionStartDate(String SubscriptionStartDate) {
            this.SubscriptionStartDate = SubscriptionStartDate;
        }

        public int getSubscriptionPrice() {
            return SubscriptionPrice;
        }

        public void setSubscriptionPrice(int SubscriptionPrice) {
            this.SubscriptionPrice = SubscriptionPrice;
        }

        public int getSubscriptionId() {
            return SubscriptionId;
        }

        public void setSubscriptionId(int SubscriptionId) {
            this.SubscriptionId = SubscriptionId;
        }

        public String getSubscriptionEndDate() {
            return SubscriptionEndDate;
        }

        public void setSubscriptionEndDate(String SubscriptionEndDate) {
            this.SubscriptionEndDate = SubscriptionEndDate;
        }

        public String getProfilePicUrl() {
            return ProfilePicUrl;
        }

        public void setProfilePicUrl(String ProfilePicUrl) {
            this.ProfilePicUrl = ProfilePicUrl;
        }

        public String getProfilePicBase64String() {
            return ProfilePicBase64String;
        }

        public void setProfilePicBase64String(String ProfilePicBase64String) {
            this.ProfilePicBase64String = ProfilePicBase64String;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public String getNotificationMessage() {
            return NotificationMessage;
        }

        public void setNotificationMessage(String NotificationMessage) {
            this.NotificationMessage = NotificationMessage;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String LastName) {
            this.LastName = LastName;
        }

        public int getIsStar() {
            return IsStar;
        }

        public void setIsStar(int IsStar) {
            this.IsStar = IsStar;
        }

        public boolean getIsSnippets() {
            return IsSnippets;
        }

        public void setIsSnippets(boolean IsSnippets) {
            this.IsSnippets = IsSnippets;
        }

        public boolean getIsRequested() {
            return IsRequested;
        }

        public void setIsRequested(boolean IsRequested) {
            this.IsRequested = IsRequested;
        }

        public boolean getIsPrivate() {
            return IsPrivate;
        }

        public void setIsPrivate(boolean IsPrivate) {
            this.IsPrivate = IsPrivate;
        }

        public boolean getIsFollowing() {
            return IsFollowing;
        }

        public void setIsFollowing(boolean IsFollowing) {
            this.IsFollowing = IsFollowing;
        }

        public boolean getIsDeactivate() {
            return IsDeactivate;
        }

        public void setIsDeactivate(boolean IsDeactivate) {
            this.IsDeactivate = IsDeactivate;
        }

        public boolean getIsAdmin() {
            return IsAdmin;
        }

        public void setIsAdmin(boolean IsAdmin) {
            this.IsAdmin = IsAdmin;
        }

        public String getInsertDateTime() {
            return InsertDateTime;
        }

        public void setInsertDateTime(String InsertDateTime) {
            this.InsertDateTime = InsertDateTime;
        }

        public int getFollowerRequestId() {
            return FollowerRequestId;
        }

        public void setFollowerRequestId(int FollowerRequestId) {
            this.FollowerRequestId = FollowerRequestId;
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String FirstName) {
            this.FirstName = FirstName;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getDeviceType() {
            return DeviceType;
        }

        public void setDeviceType(String DeviceType) {
            this.DeviceType = DeviceType;
        }

        public String getDeviceToken() {
            return DeviceToken;
        }

        public void setDeviceToken(String DeviceToken) {
            this.DeviceToken = DeviceToken;
        }

        public int getCustomerId() {
            return CustomerId;
        }

        public void setCustomerId(int CustomerId) {
            this.CustomerId = CustomerId;
        }

        public String getCloudVideoUrl() {
            return CloudVideoUrl;
        }

        public void setCloudVideoUrl(String CloudVideoUrl) {
            this.CloudVideoUrl = CloudVideoUrl;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public String getChatId() {
            return ChatId;
        }

        public void setChatId(String ChatId) {
            this.ChatId = ChatId;
        }

        public String getCategoryNames() {
            return CategoryNames;
        }

        public void setCategoryNames(String CategoryNames) {
            this.CategoryNames = CategoryNames;
        }

        public String getCategoryList() {
            return CategoryList;
        }

        public void setCategoryList(String CategoryList) {
            this.CategoryList = CategoryList;
        }

        public String getCategoryIds() {
            return CategoryIds;
        }

        public void setCategoryIds(String CategoryIds) {
            this.CategoryIds = CategoryIds;
        }

        public String getBioVideoUrl() {
            return BioVideoUrl;
        }

        public void setBioVideoUrl(String BioVideoUrl) {
            this.BioVideoUrl = BioVideoUrl;
        }

        public String getBioVideoBase64String() {
            return BioVideoBase64String;
        }

        public void setBioVideoBase64String(String BioVideoBase64String) {
            this.BioVideoBase64String = BioVideoBase64String;
        }

        public int getActiveCustomerId() {
            return ActiveCustomerId;
        }

        public void setActiveCustomerId(int ActiveCustomerId) {
            this.ActiveCustomerId = ActiveCustomerId;
        }

        public String getAboutSelf() {
            return AboutSelf;
        }

        public void setAboutSelf(String AboutSelf) {
            this.AboutSelf = AboutSelf;
        }
    }
}

