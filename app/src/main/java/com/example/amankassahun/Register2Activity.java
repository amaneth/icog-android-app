package com.example.amankassahun;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class Register2Activity extends AppCompatActivity {
private EditText reg_email_field;
    private  EditText reg_pass_field;
    private  EditText reg_confirm_pass_field;
    private Button reg_btn;
    private TextView reg_login_btn;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPass;
    private TextInputLayout tilConf;
    private ProgressBar reg_progress;
    private FirebaseAuth mAuth;
    private String email;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        mAuth= FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
reg_email_field = (EditText) findViewById(R.id.reg_email);
        reg_pass_field= (EditText) findViewById(R.id.reg_password);
        reg_confirm_pass_field= (EditText) findViewById(R.id.reg_confirm_pass);
        reg_btn= (Button) findViewById( R.id.reg_btn);
        reg_login_btn= findViewById(R.id.reg_login_btn);
        tilEmail= findViewById(R.id.reg_email_error);
        tilPass= findViewById(R.id.reg_password_error);
        tilConf= findViewById(R.id.reg_confirm_pass_error);
        reg_progress=(ProgressBar) findViewById(R.id.reg_progress);

        reg_email_field.getBackground().setColorFilter(ContextCompat.getColor(Register2Activity.this,R.color.icogGreen), PorterDuff.Mode.SRC_OUT);
        reg_pass_field.getBackground().setColorFilter(ContextCompat.getColor(Register2Activity.this,R.color.icogGreen), PorterDuff.Mode.SRC_ATOP);
        reg_confirm_pass_field.getBackground().setColorFilter(ContextCompat.getColor(Register2Activity.this,R.color.icogGreen), PorterDuff.Mode.SRC_IN);

        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 email= reg_email_field.getText().toString();
                String pass= reg_pass_field.getText().toString();
                String confirm_pass= reg_confirm_pass_field.getText().toString();
                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(confirm_pass)&&pass.length()>=6)
                {
                    if(pass.equals(confirm_pass)){
                        reg_progress.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    final FirebaseUser currrentUser=mAuth.getCurrentUser();
                                    CheckIfEmailVerified(currrentUser);
                                    startActivity(new Intent(Register2Activity.this,LoginActivity.class));




                                } else{
                                    String errorMessage= task.getException().getMessage();
                                    Toast.makeText(Register2Activity.this,"Error:"+errorMessage,Toast.LENGTH_LONG).show();
                                }
                                reg_progress.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else{
                        Toast.makeText(Register2Activity.this,"password and confirm password field not matched",Toast.LENGTH_LONG).show();
                    }
                }else if(TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(confirm_pass)){
                    tilEmail.setError("Enter your email!");
                    tilConf.setError(null);
                    if(pass.length()>=6){
                    tilPass.setError(null);}
                    else{
                        tilPass.setError("Password should contain at least 6 characters!");
                    }
                }else if(!TextUtils.isEmpty(email)&&TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(confirm_pass)){
                    tilEmail.setError(null);
                    tilConf.setError(null);
                    tilPass.setError("Enter Password!");
                }else if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)&&TextUtils.isEmpty(confirm_pass)){
                    tilEmail.setError(null);
                    tilConf.setError("Confirm your password!");
                    if(pass.length()>=6){
                        tilPass.setError(null);}
                    else{
                        tilPass.setError("Password should contain at least 6 characters!");
                    }
                }else if(TextUtils.isEmpty(email)&&TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(confirm_pass)){
                    tilEmail.setError("Enter your email!");
                    tilConf.setError(null);
                    tilPass.setError("Enter Password!");
                }else if(TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)&&TextUtils.isEmpty(confirm_pass)){
                    tilEmail.setError("Enter your email!");
                    tilConf.setError("Confirm your password!");
                    if(pass.length()>=6){
                        tilPass.setError(null);}
                    else{
                        tilPass.setError("Password should contain at least 6 characters!");
                    }
                }else if(!TextUtils.isEmpty(email)&&TextUtils.isEmpty(pass)&&TextUtils.isEmpty(confirm_pass)){
                    tilEmail.setError(null);
                    tilConf.setError("Confirm your password!");
                    tilPass.setError("Enter password");
                }else if(pass.length()<6){
                    tilEmail.setError(null);
                    tilConf.setError(null);
                    tilPass.setError("Password should contain at least 6 characters!");
                }else{
                    tilEmail.setError("Enter your email!");
                    tilPass.setError("Enter password!");
                    tilConf.setError("Confirm your password!");
                }

            }
        });

    }
    private void  CheckIfEmailVerified(final FirebaseUser user)
    {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Register2Activity.this,"Verfication email is sent to "+email,Toast.LENGTH_LONG).show();
                   }
                else{
                    String errorMessage= task.getException().getMessage();
                    Toast.makeText(Register2Activity.this,"Error:"+errorMessage,Toast.LENGTH_LONG).show();
                }
            }
        });

    }



}
