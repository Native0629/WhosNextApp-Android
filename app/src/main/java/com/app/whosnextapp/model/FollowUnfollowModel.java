package com.app.whosnextapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FollowUnfollowModel implements Serializable {

    @SerializedName("Result")
    private Integer Result;

    public Integer getResult() {
        return Result;
    }

    public void setResult(Integer Result) {
        this.Result = Result;
    }
}
