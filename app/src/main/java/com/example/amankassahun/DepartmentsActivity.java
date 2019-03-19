package com.example.amankassahun;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class DepartmentsActivity extends AppCompatActivity {
    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;
    private BottomNavigationView mainbottomNav;
    private DepartmentsFragment mDepartmentsFragment;
    private NotificationFragmentGallery notificationFragmentGallery;
    private AccountFragment accountFragment;
    private CircleImageView profileSettingImage;
    private TextView profileName;
    private static String DEPARTMENT="departmentof";
    private static String ITEMID="item_id",EMAILINDEP="email_in_depatments_activity";
    private Set<String> authority;
    private String department;
    private String email;
    private String nameOfMember;
    private String dept;
    private int itemid;
    private ImageView menuBtn;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private String image;
    private String name;
    private Button competeBtn;

    public static Intent newIntent(Context packageContext, String department,int itemId,String email){
        Intent intent = new Intent(packageContext,DepartmentsActivity.class);
        intent.putExtra(DEPARTMENT,department);
        intent.putExtra(ITEMID,itemId);
        intent.putExtra(EMAILINDEP,email);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        Intent serviceIntent= new Intent(DepartmentsActivity.this,BlogService.class);
        startService(serviceIntent);
        menuBtn=findViewById(R.id.open_menu_btn);
        competeBtn = findViewById(R.id.compete_btn_s);
        mDrawerLayout= findViewById(R.id.drawer);
        mNavigationView= findViewById(R.id.nav_view_drawer);
        View headerView= mNavigationView.getHeaderView(0);
        mainToolbar = (Toolbar) findViewById(R.id.front_toolbar);
        profileSettingImage= headerView.findViewById(R.id.profile_settings_image);
        profileName=headerView.findViewById(R.id.profile_name);
        mainbottomNav = (BottomNavigationView) findViewById(R.id.up_navigation);


        // FRAGMENTS
        mDepartmentsFragment = new DepartmentsFragment();


        notificationFragmentGallery = new NotificationFragmentGallery();
        accountFragment = new AccountFragment();
            department=getIntent().getStringExtra(DEPARTMENT);
            email= getIntent().getStringExtra(EMAILINDEP);
            itemid=getIntent().getIntExtra(ITEMID,0);
        if(itemid==2){
          competeBtn.setVisibility(View.VISIBLE);
        }
String depata=QueryPreferences.getDept(DepartmentsActivity.this);
        replaceFragment(mDepartmentsFragment.newInstance(department,itemid));
            mainbottomNav.getMenu().getItem(itemid).setChecked(true);
            boolean babo=depata.equals("public")||depata.equals("publican");
competeBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(DepartmentsActivity.this,SolvitCompetationActivity.class));
    }
});

            if(babo){
            mainbottomNav.getMenu().removeItem(R.id.iCogger_up_menu);}
        mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.makers_up_menu:
                        replaceFragment(mDepartmentsFragment.newInstance("Makers",0));
                        department="Makers";
                        competeBtn.setVisibility(View.INVISIBLE);
                        itemid=0;
                        return true;
                    case R.id.acc_up_menu:
                        replaceFragment(mDepartmentsFragment.newInstance("ACC",1));
                        department="ACC";
                        competeBtn.setVisibility(View.INVISIBLE);
                        itemid=1;
                        return true;
                    case R.id.solvit_up_menu:
                        replaceFragment(mDepartmentsFragment.newInstance("SolveIt",2));
                        department="SolveIt";
                        competeBtn.setVisibility(View.VISIBLE);
                        itemid=2;
                        return  true;
                    case R.id.die_up_menu:
                        replaceFragment(mDepartmentsFragment.newInstance("Design in Ethiopia",3));
                        department="Design in Ethiopia";
                        competeBtn.setVisibility(View.INVISIBLE);
                        itemid=3;
                        return true;
                    case R.id.iCogger_up_menu:
                        replaceFragment(mDepartmentsFragment.newInstance("icoggers",4));
                        department="icoggers";
                        competeBtn.setVisibility(View.INVISIBLE);
                        itemid=4;
                        return true;

                    default:
                        return false;
                }

            }
        });
            mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    if(!QueryPreferences.getDept(DepartmentsActivity.this).equals("public"))
                        profileSettingImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                startActivity(new Intent(DepartmentsActivity.this,SetupActivity.class));
                            }
                        });
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });





    menuBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    });
        authority= QueryPreferences.getAuthority(DepartmentsActivity.this);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_directing_btn:
                        mDrawerLayout.closeDrawers();
                       startActivity(new Intent(DepartmentsActivity.this,HomeActivity.class));
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.btn_latest_drawer:
                        mDrawerLayout.closeDrawers();
                        replaceFragment(NotificationFragmentGallery.newInstance("http://www.icog-labs.com/news/",true));
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.btn_blog_drawer:
                        mDrawerLayout.closeDrawers();
                        replaceFragment(NotificationFragmentGallery.newInstance("http://www.icog-labs.com/articles/",true));

                        return  true;
                    case R.id.btn_vacancy_drawer:
                        mDrawerLayout.closeDrawers();
                        replaceFragment(NotificationFragmentGallery.newInstance("http://www.icog-labs.com/jobs/vacancy/",true));

                        return true;
                    case R.id.btn_intern_drawer:
                        mDrawerLayout.closeDrawers();
                        replaceFragment(NotificationFragmentGallery.newInstance("http://www.icog-labs.com/about-us/apprenticeship-program/",true));

                        return true;
                    case R.id.goto_members_list:
                        mDrawerLayout.closeDrawers();

                        if(QueryPreferences.getDept(DepartmentsActivity.this).equals("public")||(authority.contains("x")&&authority.size()==1))
                            item.setVisible(false);
                        else
                            startActivity(new Intent(DepartmentsActivity.this,MemberListActivity.class));

                        return true;
                    case R.id.action_logout_btn:
                        mDrawerLayout.closeDrawers();
                        QueryPreferences.setDept(DepartmentsActivity.this,null);
                        logOut();
                        return true;
                    case R.id.action_exit:
                        mDrawerLayout.closeDrawers();
                            Exit();
                        return true;
                    case R.id.action_competa:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(DepartmentsActivity.this,SolvitCompetationActivity.class));
                        return true;

                    default:
                        return false;
                }

            }
        });}


public void Exit(){
    DepartmentsActivity.this.finish();
    System.exit(0);
}


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseuser= FirebaseAuth.getInstance().getCurrentUser();
        boolean aman= QueryPreferences.getDept(DepartmentsActivity.this).equals("public");
        if(firebaseuser==null||aman)
        { sendToLogin();

        } else {
            current_user_id= mAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(!task.getResult().exists()){
                            Intent setupIntent= SetupActivity.newIntent(DepartmentsActivity.this,nameOfMember,email,dept);
                            startActivity(setupIntent);
                            finish();
                        }
                        else{
                            RequestOptions placeHolderRequest= new RequestOptions();
                            placeHolderRequest=placeHolderRequest.placeholder(R.drawable.default_image);
                            name =task.getResult().getString("name");
                            image= task.getResult().getString("image");


                            if(image!=null){
                                Glide.with(DepartmentsActivity.this).setDefaultRequestOptions(placeHolderRequest).load(image).into(profileSettingImage);
                                profileName.setText(name);}
                            else{
                                Glide.with(DepartmentsActivity.this).setDefaultRequestOptions(placeHolderRequest).load(R.drawable.default_image).into(profileSettingImage);
                            }
                        }
                    }
                    else {
                        String errorMessage= task.getException().getMessage();
                        Toast.makeText(DepartmentsActivity.this,"Just a few seconds",Toast.LENGTH_LONG).show();
                        //Log.d("aman",errorMessage);
                        //Log.d("aman",""+errorMessage.equals("Failed to get document because the client is offline.")) ;
                        if (errorMessage.equals("Failed to get document because the client is offline.")) {
                           showDialogg();
                        }
                        }}

            });
        }

    }
    private void showDialogg(){
       /* AlertDialog.Builder builder = new AlertDialog.Builder(DepartmentsActivity.this);
        builder.setMessage("Check your internet connection please!, it may take a few seconds to listen it")
                .setCancelable(false)
                .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        replaceFragment(mDepartmentsFragment.newInstance("Makers",0));
                        department="Makers";
                        competeBtn.setVisibility(View.INVISIBLE);
                        itemid=0;

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();*/
        replaceFragment(mDepartmentsFragment.newInstance(department,itemid));

        competeBtn.setVisibility(View.INVISIBLE);


    }


    @Override
    protected void onResume() {
        super.onResume();

    }





    private void logOut() {

        mAuth.signOut();
        sendToLogin();
    }
    private void sendToLogin() {
        if(department==null){
            department="Makers";
            itemid=0;
        }
        Intent intent= LoginActivity.newIntent(DepartmentsActivity.this,department,itemid);
        startActivity(intent);
        finish();
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container,fragment);
        fragmentTransaction.commit();


    }
}
