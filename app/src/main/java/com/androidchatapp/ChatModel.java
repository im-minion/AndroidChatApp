package com.androidchatapp;

/**
 * Created by vaibhav on 1/15/18.
 */

class ChatModel {
    public ChatModel(String message, String user) {
        this.message = message;
        this.user = user;
    }

    public ChatModel() {

    }

    String message;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    String user;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
