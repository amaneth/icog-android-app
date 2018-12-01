package com.example.amankassahun;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

/**
 * Created by Aman on 9/30/2018.
 */

public class BlogPostId {
    @Exclude
    public String BlogPostId;
    public String DEPARTMENTOF;
    public <T extends BlogPostId> T withId(@NonNull final String id,@NonNull final String dept){
        this.BlogPostId=id;
        this.DEPARTMENTOF=dept;
        return (T) this;
    }
}
