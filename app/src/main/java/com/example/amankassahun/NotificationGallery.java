package com.example.amankassahun;

import android.net.Uri;

/**
 * Created by Aman on 5/7/2018.
 */

public class NotificationGallery {
    private String mTitle;
    private String mSubTitle;
    private String mId;
    private String mUrl;
    private String mContent;
    private String mReadMore;
    public String getReadMore() {
        return mReadMore;
    }

    public void setReadMore(String readMore) {
        mReadMore = readMore;
    }


    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
    }
    public String getSubTitle() {
        return mSubTitle;
    }
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getContent() {
        return mContent;
    }
    public Uri getPhotoPageUri(){
        return Uri.parse(mUrl);
    }
    public void setContent(String content) {
        mContent = content;
    }


    @Override
    public String toString() {
        return mTitle;
    }
}
