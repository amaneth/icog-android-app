package com.example.amankassahun;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class spalsh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
Thread myth=new Thread() {
    @Override
    public void run() {
        try {
sleep(1000);



 Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
            finish();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }
};
        myth.start();


}}
