package com.example.amankassahun;

import java.util.Date;

/**
 * Created by Aman on 10/1/2018.
 */

public class Comments {
    private String message ,user_id;
    private Date timestamp;
    public Comments(String message, String user_id, Date timestamp) {
        this.message = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }


    private Comments(){

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }



}
