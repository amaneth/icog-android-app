package com.example.amankassahun;

import java.security.Timestamp;
import java.util.Date;

/**
 * Created by Aman on 9/29/2018.
 */

public class BlogPost extends BlogPostId {

    public String image_url;
    public String desc;
    public String image_thumb;
    public String user_id;
    public Date timestamp;




    public BlogPost() {

    }

    public BlogPost(String image_url, String desc, String image_thumb, String user_id,Date timestamp) {
        this.image_url = image_url;
        this.desc = desc;
        this.image_thumb = image_thumb;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }


    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }





    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
