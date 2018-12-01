package com.example.amankassahun;

import java.util.List;

/**
 * Created by Aman on 10/2/2018.
 */

public class User {
    public String image;
    public String name;
    public String department;
    public List<String> authority;
    public String email;
    public String id;
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(){

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

    public User(String image, String name,String department,List<String> authority,String email,String id) {
        this.image = image;
        this.name = name;
        this.department=department;
        this.authority = authority;
        this.email=email;
        this.id=id;
    }
}
