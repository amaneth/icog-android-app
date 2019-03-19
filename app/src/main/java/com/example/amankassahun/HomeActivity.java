package com.example.amankassahun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);
        checkPlayServices(HomeActivity.this);
        switch(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)){
            case ConnectionResult.SERVICE_MISSING:
                GoogleApiAvailability.getInstance().getErrorDialog(this,ConnectionResult.SERVICE_MISSING,0).show();
                        break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                GoogleApiAvailability.getInstance().getErrorDialog(this,ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,0).show();
                break;
            case ConnectionResult.SERVICE_DISABLED:
                GoogleApiAvailability.getInstance().getErrorDialog(this,ConnectionResult.SERVICE_DISABLED,0).show();
                break;
            case ConnectionResult.API_UNAVAILABLE:
                GoogleApiAvailability.getInstance().getErrorDialog(this,ConnectionResult.API_UNAVAILABLE,0).show();
                break;
            case ConnectionResult.API_VERSION_UPDATE_REQUIRED:
                GoogleApiAvailability.getInstance().getErrorDialog(this,ConnectionResult.API_VERSION_UPDATE_REQUIRED,0).show();
                break;
            case ConnectionResult.CANCELED:
                GoogleApiAvailability.getInstance().getErrorDialog(this,ConnectionResult.CANCELED,0).show();
                break;
            case ConnectionResult.SIGN_IN_FAILED:
                GoogleApiAvailability.getInstance().getErrorDialog(this,ConnectionResult.SIGN_IN_REQUIRED,0).show();
                break;
            case ConnectionResult.SIGN_IN_REQUIRED:
                GoogleApiAvailability.getInstance().getErrorDialog(this,ConnectionResult.SIGN_IN_REQUIRED,0).show();
                break;
            case ConnectionResult.NETWORK_ERROR:
                GoogleApiAvailability.getInstance().getErrorDialog(this,ConnectionResult.NETWORK_ERROR,0).show();
                break;
            case ConnectionResult.SERVICE_INVALID:
                GoogleApiAvailability.getInstance().getErrorDialog(this,ConnectionResult.SERVICE_INVALID,0).show();
                break;

        }
        firebaseFirestore=FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseuser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseuser==null&&mAuth!=null)
        {
            mAuth.signInWithEmailAndPassword("icog@gmail.com","password");


        }
        /*GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            try {
                                int ps= getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE,0).versionCode;
                                Toast.makeText(HomeActivity.this,"PS OK!"+ps+","+ps/1000000,Toast.LENGTH_LONG).show();
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                            mAuth= FirebaseAuth.getInstance();
                            FirebaseUser firebaseuser= FirebaseAuth.getInstance().getCurrentUser();
                            if(firebaseuser==null&&mAuth!=null)
                            {
                                mAuth.signInWithEmailAndPassword("icog@gmail.com","password");


                            }
                        }else{
                            try {
                                Toast.makeText(HomeActivity.this,"PS  Not OK!"+getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE,0).versionCode,Toast.LENGTH_LONG).show();
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
*/

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
        mToolbar=findViewById(R.id.front_toolbar);
        frontBottomNav= findViewById(R.id.front_bottom_nav);
        mNavigationView= findViewById(R.id.nav_viewy);

        mAuth=FirebaseAuth.getInstance();
        setSupportActionBar(mToolbar);
        replaceFragment(new HomeFragment(),"Home");


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
                    case R.id.action_competee:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(HomeActivity.this,SolvitCompetationActivity.class));
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

    }
    public static boolean checkPlayServices(Context context) {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int resultCode = api.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (api.isUserResolvableError(resultCode))
                api.getErrorDialog(((Activity) context), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            else {
                Toast.makeText(context, "This device is not supported.", Toast.LENGTH_LONG).show();
                ((Activity) context).finish();
            }
            return false;
        }
        return true;
    }
    private void replaceFragment(Fragment fragment, String title){
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.front_container,fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(title);

    }
}
