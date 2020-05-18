package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddUserInGroupVideo implements Serializable {
    @SerializedName("Result")
    private Boolean Result;

    public Boolean getResult() {
        return Result;
    }

    public void setResult(Boolean Result) {
        this.Result = Result;
    }
}
