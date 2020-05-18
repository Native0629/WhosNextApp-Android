package com.app.whosnextapp.model;

import java.io.Serializable;

public class TagListModel implements Serializable {

    private String Username;
    private Float Xcordinate;
    private Float Ycordinate;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public Float getXcordinate() {
        return Xcordinate;
    }

    public void setXcordinate(Float xcordinate) {
        Xcordinate = xcordinate;
    }

    public Float getYcordinate() {
        return Ycordinate;
    }

    public void setYcordinate(Float ycordinate) {
        Ycordinate = ycordinate;
    }
}
