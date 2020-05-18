package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddSubscriptionsModel implements Serializable {
    @SerializedName("SubscriptionResult")
    private String SubscriptionResult;

    public String getSubscriptionResult() {
        return SubscriptionResult;
    }

    public void setSubscriptionResult(String SubscriptionResult) {
        this.SubscriptionResult = SubscriptionResult;
    }
}
