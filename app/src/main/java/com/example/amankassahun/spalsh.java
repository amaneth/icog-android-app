package com.example.amankassahun;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class spalsh extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        firebaseFirestore= FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        //firebaseFirestore.setFirestoreSettings(settings);
        firebaseFirestore.setFirestoreSettings(settings);
        //FirebaseFirestore.setLoggingEnabled(true);
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
