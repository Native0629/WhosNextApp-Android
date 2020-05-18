package com.app.whosnextapp.apis;

public interface ResponseListener {
    void onSucceedToPostCall(String response);

    void onFailedToPostCall(int statusCode, String msg);
}
