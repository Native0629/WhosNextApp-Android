package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegistrationModel implements Serializable {

    @SerializedName("Status")
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public class Status implements Serializable {

        @SerializedName("ChatId")
        private Object chatId;
        @SerializedName("CustomerId")
        private Object customerId;
        @SerializedName("Email")
        private Object email;
        @SerializedName("IsAdmin")
        private Boolean isAdmin;
        @SerializedName("IsSnippets")
        private Boolean isSnippets;
        @SerializedName("Name")
        private Object name;
        @SerializedName("NotificationCount")
        private Object notificationCount;
        @SerializedName("ProfilePicUrl")
        private Object profilePicUrl;
        @SerializedName("Result")
        private String result;
        @SerializedName("Timer")
        private Integer timer;
        @SerializedName("TotalFollowers")
        private Object totalFollowers;
        @SerializedName("TotalFollowings")
        private Object totalFollowings;
        @SerializedName("TotalPost")
        private Object totalPost;
        @SerializedName("UserId")
        private Object userId;
        @SerializedName("UserName")
        private Object userName;
        @SerializedName("VerificationCode")
        private Object verificationCode;

        public Object getChatId() {
            return chatId;
        }

        public void setChatId(Object chatId) {
            this.chatId = chatId;
        }

        public Object getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Object customerId) {
            this.customerId = customerId;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Boolean getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(Boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public Boolean getIsSnippets() {
            return isSnippets;
        }

        public void setIsSnippets(Boolean isSnippets) {
            this.isSnippets = isSnippets;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getNotificationCount() {
            return notificationCount;
        }

        public void setNotificationCount(Object notificationCount) {
            this.notificationCount = notificationCount;
        }

        public Object getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(Object profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public Integer getTimer() {
            return timer;
        }

        public void setTimer(Integer timer) {
            this.timer = timer;
        }

        public Object getTotalFollowers() {
            return totalFollowers;
        }

        public void setTotalFollowers(Object totalFollowers) {
            this.totalFollowers = totalFollowers;
        }

        public Object getTotalFollowings() {
            return totalFollowings;
        }

        public void setTotalFollowings(Object totalFollowings) {
            this.totalFollowings = totalFollowings;
        }

        public Object getTotalPost() {
            return totalPost;
        }

        public void setTotalPost(Object totalPost) {
            this.totalPost = totalPost;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public Object getUserName() {
            return userName;
        }

        public void setUserName(Object userName) {
            this.userName = userName;
        }

        public Object getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(Object verificationCode) {
            this.verificationCode = verificationCode;
        }
    }
}
