package com.example.amankassahun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


/**
 * Created by Aman on 5/10/2018.
 */

public class AboutActivity extends AppCompatActivity {

Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        mToolbar=findViewById(R.id.about_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }





}
