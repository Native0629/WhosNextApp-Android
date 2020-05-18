package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDetailModel implements Serializable {

    @SerializedName("Status")
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static class Status implements Serializable {

        @SerializedName("ChatId")
        private int chatId;

        @SerializedName("CustomerId")
        private String customerId;

        @SerializedName("Email")
        private String email;

        @SerializedName("IsAdmin")
        private boolean isAdmin;

        @SerializedName("IsSnippets")
        private boolean isSnippets;

        @SerializedName("Name")
        private String name;

        @SerializedName("NotificationCount")
        private int notificationCount;

        @SerializedName("ProfilePicUrl")
        private String profilePicUrl;

        @SerializedName("Result")
        private String result;

        @SerializedName("Timer")
        private int timer;

        @SerializedName("TotalFollowers")
        private String totalFollowers;

        @SerializedName("TotalFollowings")
        private String totalFollowings;

        @SerializedName("TotalPost")
        private String totalPost;

        @SerializedName("UserId")
        private String userId;

        @SerializedName("UserName")
        private String userName;

        @SerializedName("VerificationCode")
        private String verificationCode;

        private String password;

        public int getChatId() {
            return chatId;
        }

        public void setChatId(int chatId) {
            this.chatId = chatId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public boolean isIsSnippets() {
            return isSnippets;
        }

        public void setIsSnippets(boolean isSnippets) {
            this.isSnippets = isSnippets;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNotificationCount() {
            return notificationCount;
        }

        public void setNotificationCount(int notificationCount) {
            this.notificationCount = notificationCount;
        }

        public String getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public int getTimer() {
            return timer;
        }

        public void setTimer(int timer) {
            this.timer = timer;
        }

        public String getTotalFollowers() {
            return totalFollowers;
        }

        public void setTotalFollowers(String totalFollowers) {
            this.totalFollowers = totalFollowers;
        }

        public String getTotalFollowings() {
            return totalFollowings;
        }

        public void setTotalFollowings(String totalFollowings) {
            this.totalFollowings = totalFollowings;
        }

        public String getTotalPost() {
            return totalPost;
        }

        public void setTotalPost(String totalPost) {
            this.totalPost = totalPost;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}






