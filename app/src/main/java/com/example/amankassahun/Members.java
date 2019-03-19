package com.example.amankassahun;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Aman on 10/2/2018.
 */

public class Members {
    String name,email,department;
    public Members(){

    }
   /* public class MyApplication extends Application {



        @Override
        public void onCreate() {
            super.onCreate();
            Fabric.with(this,new Crashlytics());

        }
    }*/
    public Members(String name, String email, String department) {
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
