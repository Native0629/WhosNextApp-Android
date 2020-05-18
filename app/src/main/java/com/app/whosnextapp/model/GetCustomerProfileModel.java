package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetCustomerProfileModel implements Serializable {

    @SerializedName("CustomerDetail")
    private CustomerDetail CustomerDetail;

    public CustomerDetail getCustomerDetail() {
        return CustomerDetail;
    }

    public void setCustomerDetail(CustomerDetail CustomerDetail) {
        this.CustomerDetail = CustomerDetail;
    }

    public static class CustomerDetail implements Serializable {
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
        @SerializedName("FollowerRequestId")
        private int FollowerRequestId;
        @SerializedName("FirstName")
        private String FirstName;
        @SerializedName("Email")
        private String Email;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("City")
        private String City;
        @SerializedName("CategoryNames")
        private String CategoryNames;
        @SerializedName("CategoryList")
        private List<CategoryList> CategoryList;
        @SerializedName("CategoryIds")
        private String CategoryIds;
        @SerializedName("BioVideoUrl")
        private String BioVideoUrl;
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

        public int getCustomerId() {
            return CustomerId;
        }

        public void setCustomerId(int CustomerId) {
            this.CustomerId = CustomerId;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public String getCategoryNames() {
            return CategoryNames;
        }

        public void setCategoryNames(String CategoryNames) {
            this.CategoryNames = CategoryNames;
        }

        public List<CategoryList> getCategoryList() {
            return CategoryList;
        }

        public void setCategoryList(List<CategoryList> CategoryList) {
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

    public static class CategoryList implements Serializable {
        @SerializedName("CategoryName")
        private String CategoryName;
        @SerializedName("CategoryId")
        private int CategoryId;

        public String getCategoryName() {
            return CategoryName;
        }

        public void setCategoryName(String CategoryName) {
            this.CategoryName = CategoryName;
        }

        public int getCategoryId() {
            return CategoryId;
        }

        public void setCategoryId(int CategoryId) {
            this.CategoryId = CategoryId;
        }
    }
}
