package com.example.amankassahun;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmailText;
    private EditText loginPassText;
    private TextView welcomeTxt;
    private Button loginBtn;
    private Button verifyBtn;
    private Button guestLoginButton;
    private ProgressBar loginProgress;
    private TextView loginRegBtn;
    private TextView forgetPassword;
    private TextInputLayout til;
    private TextInputLayout tilPass;
    private FirebaseAuth mAuth;
    private String department;
    private String deppt;

    private String loginEmail;
    private DocumentReference docRef;
    private FirebaseFirestore firebaseFirestore;
    private List<Members> members_list;
    private List<String> authority;
    private static String DEPARTMENTA="departmentalogin";
    private static String ITEMIDA="ddepartmentaposition";
    public static Intent newIntent(Context packageContext, String department, int itemId){
        Intent intent = new Intent(packageContext,LoginActivity.class);
        intent.putExtra(DEPARTMENTA,department);
        intent.putExtra(ITEMIDA,itemId);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        members_list = new ArrayList<>();
        loginEmailText = (EditText) findViewById(R.id.login_email);
        verifyBtn= findViewById(R.id.verifyButton);
        loginPassText = (EditText) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginRegBtn =  findViewById(R.id.login_reg_btn);
        forgetPassword= findViewById(R.id.forget_password);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);

        welcomeTxt=findViewById(R.id.welcome_txt);
        til=  findViewById(R.id.login_error);
        tilPass=findViewById(R.id.login_password_error);
        department = getIntent().getStringExtra(DEPARTMENTA);
        if(department!=null){
        welcomeTxt.setText(department);}
        loginEmailText.getBackground().setColorFilter(ContextCompat.getColor(LoginActivity.this,R.color.icogGreen), PorterDuff.Mode.SRC_ATOP);
        loginPassText.getBackground().setColorFilter(ContextCompat.getColor(LoginActivity.this,R.color.icogGreen), PorterDuff.Mode.SRC_OVER);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPassText.getBackground().setColorFilter(ContextCompat.getColor(LoginActivity.this,R.color.icogGreen), PorterDuff.Mode.SRC_ATOP);

                loginEmail = loginEmailText.getText().toString();
                if(!TextUtils.isEmpty(loginEmail)){
                mAuth.sendPasswordResetEmail(loginEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Email sent to "+loginEmail,Toast.LENGTH_LONG).show();
                        }else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, "error: " + errorMessage, Toast.LENGTH_LONG).show();

                        }

                    }
                });}else{
                    til.setError("Enter your email!");
                    tilPass.setError(null);
                }
            }
        });

        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(LoginActivity.this, Register2Activity.class);
                startActivity(regIntent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginEmail = loginEmailText.getText().toString();
                String loginPass = loginPassText.getText().toString();
                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)) {
                    //Toast.makeText(LoginActivity.this,"Goona start",Toast.LENGTH_SHORT).show();
                    loginProgress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(LoginActivity.this,"success",Toast.LENGTH_SHORT).show();

                                String current_user_id= mAuth.getCurrentUser().getUid();
                                if(mAuth.getCurrentUser().isEmailVerified()){
                                 docRef=firebaseFirestore.collection("Users").document(current_user_id);
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            if(task.getResult().exists()){

                                                authority= (List<String>) task.getResult().get("authority");
                                                 deppt= task.getResult().getString("department");
                                                Set<String> set= new HashSet<>();
                                                set.addAll(authority);
                                                QueryPreferences.setAuthority(LoginActivity.this,set);
                                                QueryPreferences.setDept(LoginActivity.this, deppt);
                                                SendToMain();

                                         }else {
                                                QueryPreferences.setDept(LoginActivity.this, "publican");
                                                SendToMain();
                                            }
                                        }else {
                                            QueryPreferences.setDept(LoginActivity.this, "publican");
                                            SendToMain();
                                        }

                                    }
                                });}
                                else {
                                    Toast.makeText(LoginActivity.this,"Your account has not verified by email verification",Toast.LENGTH_LONG).show();
                                    verifyBtn.setVisibility(View.VISIBLE);
                                }


                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "error: " + errorMessage, Toast.LENGTH_LONG).show();

                            }
                            loginProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                } else if(!TextUtils.isEmpty(loginPass)&&TextUtils.isEmpty(loginEmail)){
                 til.setError("Enter your email!");
                    tilPass.setError(null);
                }else if(TextUtils.isEmpty(loginPass)&&!TextUtils.isEmpty(loginEmail)) {
                    tilPass.setError("Enter your password!");
                    til.setError(null);
                }else{
                    tilPass.setError("Enter your password!");
                    til.setError("Enter your email");
                }
            }
        });
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Email verification is sent again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    protected void onStart() {
        super.onStart();
    }
    private void SendToMain(){
        if(authority!=null){
        Intent mainintent= DepartmentsActivity.newIntent(LoginActivity.this,department,getIntent().getIntExtra(ITEMIDA,0),loginEmail);
        startActivity(mainintent);
        finish();}
        else{
            startActivity(SetupActivity.newIntent(LoginActivity.this,null,loginEmail,null));
            finish();
        }
    }
}
