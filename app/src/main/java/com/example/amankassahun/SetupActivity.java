package com.example.amankassahun;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import id.zelory.compressor.Compressor;

public class SetupActivity extends AppCompatActivity {
    private ImageView setupImage;
    private Uri mainImageUri=null;
    private Uri imageUri=null;
    private Bitmap compressedImageFile=null;
    private String user_id;
    private EditText setupName;
    private boolean isChanged=false;
    private Button setupBtn;
    private ProgressBar setupProgress;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private String emaila;
    private String departmenta;
    private static String NAME="nameofnewuser";
    private static String EMAIL="emailofuser";
    private static String DEPARTMENTSETUPI="departmentinsetupo";
     FirebaseFirestore firebaseFirestore;
    public static Intent newIntent(Context packageContext, String name,String email,String department){
        Intent intent = new Intent(packageContext,SetupActivity.class);
        intent.putExtra(NAME,name);
        intent.putExtra(EMAIL,email);
        intent.putExtra(DEPARTMENTSETUPI,department);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar setUpToolbar= (Toolbar) findViewById(R.id.web_view_toolbar);
        setSupportActionBar(setUpToolbar);
        getSupportActionBar().setTitle("Account setup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth=FirebaseAuth.getInstance();
        user_id=firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore= FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        setupImage= (ImageView) findViewById(R.id.setup_image);
        setupName= (EditText) findViewById(R.id.setup_name);
        setupBtn= (Button) findViewById(R.id.setup_btn);
        setupProgress= (ProgressBar) findViewById(R.id.setup_progress);
        setupProgress.setVisibility(View.VISIBLE);
        setupBtn.setEnabled(false);
     emaila= getIntent().getStringExtra(EMAIL);
       departmenta=getIntent().getStringExtra(DEPARTMENTSETUPI);

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){

               String name =task.getResult().getString("name");
                String image= task.getResult().getString("image");
                        setupName.setText(name);
                        if(image!=null){
                        mainImageUri=Uri.parse(image);
                        RequestOptions placeHolderRequest= new RequestOptions();
                        placeHolderRequest=placeHolderRequest.placeholder(R.drawable.default_image);
                        Glide.with(SetupActivity.this).setDefaultRequestOptions(placeHolderRequest).load(image).into(setupImage);
                    }else{
                            Glide.with(SetupActivity.this).load(R.drawable.default_image).into(setupImage);
                        }}
                }
                else{
                    String namereterieved=getIntent().getStringExtra(NAME);
                    if(namereterieved!=null){
                    setupName.setText(namereterieved);}
                    String error= task.getException().getMessage();
                    Toast.makeText(SetupActivity.this,"FIRESTORE RETERIEVE ERROR: "+error,Toast.LENGTH_LONG).show();
                }
                setupProgress.setVisibility(View.INVISIBLE);
                setupBtn.setEnabled(true);
            }
        });
        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name= setupName.getText().toString();
                setupProgress.setVisibility(View.VISIBLE);
                if(isChanged){

                if(!TextUtils.isEmpty(user_name)&&mainImageUri!=null){
                 user_id= firebaseAuth.getCurrentUser().getUid();
                    File newImagefile= new File(mainImageUri.getPath());
                    try {
                        Log.d("newpost","compressing started");
                        compressedImageFile= new Compressor(SetupActivity.this)
                                .setMaxHeight(100)
                                .setMaxWidth(100)
                                .setQuality(0)
                                .compressToBitmap(newImagefile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream out= new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG,0,out);
                    imageUri=getImageUri(getApplicationContext(),compressedImageFile);
                    StorageReference image_path= storageReference.child("profile_images").child(user_id+".jpg");

                    image_path.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                storeFireStore(task,user_name);

                            }else{
                                String error= task.getException().getMessage();
                                Toast.makeText(SetupActivity.this,"Image error"+error,Toast.LENGTH_LONG).show();
                                setupProgress.setVisibility(View.INVISIBLE);
                            }


                        }
                    });

                }else if(mainImageUri==null){
                    storeFireStore(null,user_name);
                }}
            else{
                storeFireStore(null,user_name);
            }
            }
        });
        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(SetupActivity.this,"PERMISSION DENIED",Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                    }
                    else{
                       BringImagePicker();
                    }
                }
                else{
                    BringImagePicker();
                }
            }
        });


    }
    private Uri getImageUri(Context inContext, Bitmap selectedImage) {
        ByteArrayOutputStream bytes= new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),selectedImage,"Title ",null);

        if(path!=null)
        {
            Uri imageUrii= Uri.parse(path);
            return imageUrii;
        }
        return null;
    }
    private void storeFireStore(@NonNull Task<UploadTask.TaskSnapshot> task,String user_name) {

        Uri download_uri;
        if(task!=null){
        download_uri=task.getResult().getDownloadUrl();}
        else{
            download_uri=mainImageUri;
        }
        Map<String,Object> userMap= new HashMap<>();
        userMap.put("name",user_name);
        if(download_uri!=null){
        userMap.put("image",download_uri.toString());}
        else{
            userMap.put("image",null);
        }

        if(departmenta!=null){
        userMap.put("department",departmenta);}
        else{
            userMap.put("department","-");
        }
        if(emaila!=null){
        userMap.put("email",emaila);}
        else{
            userMap.put("email","unknown");
        }

        userMap.put("authority", Arrays.asList("x"));
        userMap.put("id",user_id);
        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SetupActivity.this,"successfuly updated",Toast.LENGTH_LONG).show();
                    Intent mainIntent= new Intent(SetupActivity.this,DepartmentsActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
                else{
                    String error= task.getException().getMessage();
                    Toast.makeText(SetupActivity.this,"FIRESTORE ERROR: "+error,Toast.LENGTH_LONG).show();

                }
                setupProgress.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(SetupActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                mainImageUri=result.getUri();
                setupImage.setImageURI(mainImageUri);
                isChanged=true;
            }else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error= result.getError();
                Toast.makeText(SetupActivity.this,"error:"+error,Toast.LENGTH_LONG).show();
            }
        }
    }
}
