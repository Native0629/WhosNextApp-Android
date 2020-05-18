package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectPeopleModel implements Serializable {

    @SerializedName("CustomerList")
    private ArrayList<CustomerList> customerList = null;

    public ArrayList<CustomerList> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(ArrayList<CustomerList> customerList) {
        this.customerList = customerList;
    }

    public static class CustomerList implements Serializable {

        public boolean isSelected = false;
        private String Username;


        public CustomerList(String username, boolean isSelected) {
            Username = username;
            this.isSelected = isSelected;
        }

        public String getUsername() {
            return Username;
        }

        public void setUsername(String username) {
            Username = username;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
