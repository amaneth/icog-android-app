package com.example.amankassahun;

import java.util.List;

/**
 * Created by Aman on 10/14/2018.
 */


public class Commentor {
    public String image;
    public String name;
    public String department;
    public List<String> authority;
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public Commentor(){

    }

    public List<String> getAuthority() {
        return authority;
    }

    public void setAuthority(List<String> authority) {
        this.authority = authority;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Commentor(String image, String name,String department,List<String> authority) {
        this.image = image;
        this.name = name;
        this.department=department;
        this.authority = authority;
    }
}

