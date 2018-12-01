package com.example.amankassahun;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView frontBottomNav;
    private Toolbar mToolbar;
    FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private boolean isDrawerOpened;
    private MaterialMenuDrawable materialMenu;
    private NavigationView mNavigationView;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;

    private Button Hiruy;
    private boolean fixed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseuser= FirebaseAuth.getInstance().getCurrentUser();
        Log.d("fireb",""+firebaseuser);
        if(firebaseuser==null&&mAuth!=null)
        {
            mAuth.signInWithEmailAndPassword("icog@gmail.com","password");


        }

       /* Button crashButton= new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.getInstance().crash();
            }
        });
        addContentView(crashButton,new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        ));*/
       Hiruy=findViewById(R.id.hiruy);
        mToolbar=findViewById(R.id.front_toolbar);
        frontBottomNav= findViewById(R.id.front_bottom_nav);
        mNavigationView= findViewById(R.id.nav_viewy);
        firebaseFirestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        setSupportActionBar(mToolbar);
        replaceFragment(new HomeFragment(),"Home");
        if(QueryPreferences.getHiruy(HomeActivity.this)){
            Hiruy.setText("what was before fixed?");
        }else{
            Hiruy.setText("return to After fixed");
        }
        Hiruy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(QueryPreferences.getHiruy(HomeActivity.this)){

                    Toast.makeText(HomeActivity.this,"it will crash on clicking buttons",Toast.LENGTH_SHORT).show();

                    QueryPreferences.setHiruy(HomeActivity.this,false);
                    Hiruy.setText("return to After fixed");
                    }
                else {
                    Hiruy.setText("what was before fixed?");
                    Toast.makeText(HomeActivity.this,"no more crashes",Toast.LENGTH_SHORT).show();
                    QueryPreferences.setHiruy(HomeActivity.this,true);
                }
            }
        });
        if(mAuth.getCurrentUser()!=null){
        current_user_id= mAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Users").document(current_user_id)
        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        List<String> authority;
                        authority= (List<String>) task.getResult().get("authority");
                        Set<String> set= new HashSet<>();
                        set.addAll(authority);
                        QueryPreferences.setAuthority(HomeActivity.this,set);}}}});}
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // Handle your drawable state here
                if(!isDrawerOpened) {

                    materialMenu.setIconState(MaterialMenuDrawable.IconState.ARROW);
                    drawerLayout.openDrawer(Gravity.LEFT);
                } else {
                    materialMenu.setIconState(MaterialMenuDrawable.IconState.BURGER);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }

            }
        });
        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        mToolbar.setNavigationIcon(materialMenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layouty);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                materialMenu.setTransformationOffset(
                        MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        isDrawerOpened ? 2 - slideOffset : slideOffset
                );
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawerOpened = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if(newState == DrawerLayout.STATE_IDLE) {
                    if(isDrawerOpened) {
                        materialMenu.setIconState(MaterialMenuDrawable.IconState.ARROW);
                    } else {
                        materialMenu.setIconState(MaterialMenuDrawable.IconState.BURGER);
                    }
                }
            }
        });

        frontBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_action_home:
                        replaceFragment(new HomeFragment(),"Home");
                        return true;
                    case R.id.bottom_action_internship:
                        replaceFragment(NotificationFragmentGallery.newInstance("http://www.icog-labs.com/about-us/apprenticeship-program/",false),"Internship program");
                        return true;
                    case R.id.bottom_action_vacancy:
                        replaceFragment(NotificationFragmentGallery.newInstance("http://www.icog-labs.com/jobs/vacancy/",false),"Vacancy");
                        return  true;
                    case R.id.bottom_action_blog:
                        replaceFragment(NotificationFragmentGallery.newInstance("http://www.icog-labs.com/articles/",false),"Blog");
                        return true;
                    case R.id.bottom_action_latest_news:
                        replaceFragment(NotificationFragmentGallery.newInstance("http://www.icog-labs.com/news/",false),"Latest news");
                        return true;
                    default:
                        return false;
                }

            }
        });
        MenuItem toggleItem = mNavigationView.getMenu().findItem(R.id.menu_item_toggle_polling);
        if (PollService.isServiceAlarmOn(HomeActivity.this)) {
            toggleItem.setTitle(R.string.close_notifications);
        } else {
            toggleItem.setTitle(R.string.open_notifications);

    }
        MenuItem logItem = mNavigationView.getMenu().findItem(R.id.action_logout);
        if (QueryPreferences.getDept(HomeActivity.this).equals("public")) {
            logItem.setTitle("Login");
        } else {
            logItem.setTitle("Logout");

        }
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_like_usf:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(HomeActivity.this,LikeUsActivity.class));
                        return true;
                    case R.id.menu_item_about:
                        drawerLayout.closeDrawers();
                        Intent intent= new Intent(HomeActivity.this,AboutActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_logout:
                        drawerLayout.closeDrawers();
                        QueryPreferences.setDept(HomeActivity.this,null);
                        mAuth.signOut();
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        finish();
                        return true;
                    case R.id.menu_item_toggle_polling:
                        boolean shouldStartAlarm = !PollService.isServiceAlarmOn(HomeActivity.this);
                        PollService.setServiceAlarm(HomeActivity.this, shouldStartAlarm);
                        HomeActivity.this.invalidateOptionsMenu();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        recreate();
                        return true;
                    case R.id.action_exit:
                        drawerLayout.closeDrawers();
                        Exit();
                        return true;
                    default:
                        return false;
                }

            }
        });
       /* final Fabric fabric= new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);*/

    }
    public void Exit(){
        HomeActivity.this.finish();
        System.exit(0);
    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.d("fireb",""+QueryPreferences.getDept(HomeActivity.this));
    }

    private void replaceFragment(Fragment fragment, String title){
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.front_container,fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(title);

    }
}
