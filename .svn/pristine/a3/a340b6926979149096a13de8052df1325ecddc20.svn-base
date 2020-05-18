package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupListModel implements Serializable {

    @SerializedName("CategoryList")
    private ArrayList<GroupListModel.UserList> userLists = null;

    public ArrayList<GroupListModel.UserList> getUserLists() {
        return userLists;
    }

    public void setUserLists(ArrayList<GroupListModel.UserList> userLists) {
        this.userLists = userLists;
    }

    public static class UserList implements Serializable {

        private String Username;
        private String ProfilePic;
        private Integer CustomerId;
        private Integer Position;


        public String getUsername() {
            return Username;
        }

        public void setUsername(String username) {
            Username = username;
        }

        public String getProfilePic() {
            return ProfilePic;
        }

        public void setProfilePic(String profilePic) {
            ProfilePic = profilePic;
        }

        public Integer getCustomerId() {
            return CustomerId;
        }

        public void setCustomerId(Integer customerId) {
            CustomerId = customerId;
        }

        public Integer getPosition() {
            return Position;
        }

        public void setPosition(Integer position) {
            Position = position;
        }
    }
}
