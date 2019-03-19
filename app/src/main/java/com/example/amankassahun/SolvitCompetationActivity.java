package com.example.amankassahun;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SolvitCompetationActivity extends AppCompatActivity {
private  Toolbar setUpToolbar;
    private ImageButton drawerBtn;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TextView mRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solvit_competation);
         setUpToolbar=  findViewById(R.id.compete_toolbar);
        drawerBtn= findViewById(R.id.compete_drawer_btn);
        mDrawerLayout= findViewById(R.id.drawer_layout_compete);
        mNavigationView = findViewById(R.id.nav_view_compete);
        mRegister=findViewById(R.id.register_for_compet);
        setSupportActionBar(setUpToolbar);
        getSupportActionBar().setTitle("SolveIt Competition");
        drawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PhotoPageActivity.newIntent(SolvitCompetationActivity.this, Uri.parse("https://icog-solveit.com/register")));
            }
        });
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_btn_compete:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(SolvitCompetationActivity.this,HomeActivity.class));
                        return true;
                    case R.id.service_btn_compete:
                        mDrawerLayout.closeDrawers();
                        startActivity(PhotoPageActivity.newIntent(SolvitCompetationActivity.this, Uri.parse("https://icog-solveit.com/#service")));

                        return true;
                    case R.id.event_btn_compete:
                        mDrawerLayout.closeDrawers();
                        startActivity(PhotoPageActivity.newIntent(SolvitCompetationActivity.this, Uri.parse("https://icog-solveit.com/events")));


                        return true;
                    case R.id.forum_btn_compete:
                        mDrawerLayout.closeDrawers();
                        startActivity(PhotoPageActivity.newIntent(SolvitCompetationActivity.this, Uri.parse("https://icog-solveit.com/forums")));
                        return true;
                    case R.id.news_btn_compete:
                        mDrawerLayout.closeDrawers();
                        startActivity(PhotoPageActivity.newIntent(SolvitCompetationActivity.this, Uri.parse("https://icog-solveit.com/news")));
                        return true;
                    case R.id.sign_btn_compete:
                        mDrawerLayout.closeDrawers();
                        startActivity(PhotoPageActivity.newIntent(SolvitCompetationActivity.this, Uri.parse("https://icog-solveit.com/login")));
                        return true;

                    default:
                        return false;
                }

            }
        });

    }
}
