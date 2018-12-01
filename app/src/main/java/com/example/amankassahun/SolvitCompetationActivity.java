package com.example.amankassahun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class SolvitCompetationActivity extends AppCompatActivity {
private  Toolbar setUpToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solvit_competation);
         setUpToolbar=  findViewById(R.id.compete_toolbar);
        setSupportActionBar(setUpToolbar);
        getSupportActionBar().setTitle("Account setup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
