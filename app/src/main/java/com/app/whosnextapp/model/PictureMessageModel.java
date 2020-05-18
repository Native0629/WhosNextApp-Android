package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PictureMessageModel implements Serializable {
    @SerializedName("CategoryList")
    private ArrayList<PictureMessageModel.CustomerList> customerLists = null;

    public ArrayList<PictureMessageModel.CustomerList> getCustomerLists() {
        return customerLists;
    }

    public void setCategoryList(ArrayList<PictureMessageModel.CustomerList> customerLists) {
        this.customerLists = customerLists;
    }

    public static class CustomerList implements Serializable {
        public boolean isSelected;
        @SerializedName("UserName")
        private String UserName;
        @SerializedName("CustomerId")
        private int CustomerId;
        @SerializedName("ChatId")
        private String ChatId;

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public int getCustomerId() {
            return CustomerId;
        }

        public void setCustomerId(int customerId) {
            CustomerId = customerId;
        }

        public String getChatId() {
            return ChatId;
        }

        public void setChatId(String chatId) {
            ChatId = chatId;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}