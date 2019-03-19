package com.example.amankassahun;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewPostActivity extends AppCompatActivity {
    private Toolbar newPostToolbar;
    private ImageView newPostImage;
    private EditText newPostDesc;
    private Button newPostBtn;
    private Uri postImageUri=null;
    private Uri imageUri=null;
    private ProgressBar newPostProgress;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    private Bitmap compressedImageFile;
    private static String DEPARTMENTPOST="departmentforpost";
    private static String ITEMPOSITION="item_position";

    public static Intent newIntentPost(Context packageContext, String department,int item){
        Intent intent = new Intent(packageContext,NewPostActivity.class);
        intent.putExtra(DEPARTMENTPOST,department);
        intent.putExtra(ITEMPOSITION,item);

        return intent;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        final String randomName= UUID.randomUUID().toString();
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        current_user_id=firebaseAuth.getCurrentUser().getUid();
        newPostToolbar=findViewById(R.id.new_post_toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        newPostImage= (ImageView) findViewById(R.id.new_post_image);
        newPostDesc= (EditText) findViewById(R.id.new_post_desc);
        newPostBtn= (Button) findViewById(R.id.post_btn);
        newPostBtn.setEnabled(true);

        newPostProgress =(ProgressBar) findViewById(R.id.new_post_progress);




        newPostImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(NewPostActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(NewPostActivity.this,"PERMISSION DENIED",Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(NewPostActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                    }
                    else{
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)


                                .start(NewPostActivity.this);
                    }
                }
                else{
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)


                            .start(NewPostActivity.this);
                }

            }
        });

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desc= newPostDesc.getText().toString();
                if(postImageUri!=null){
                    newPostProgress.setVisibility(View.VISIBLE);
                    newPostBtn.setEnabled(false);

                    if(postImageUri!=null){
                    File newImagefile= new File(postImageUri.getPath());
                    try {
                        Log.d("newpost","compressing started");
                        compressedImageFile= new Compressor(NewPostActivity.this)
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
                    final StorageReference filePath= storageReference.child("post_images").child(randomName+".jpg");
                     filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                             final String downloadUrl= task.getResult().getDownloadUrl().toString();
                             if(task.isSuccessful()){



                                         Map<String,Object> postMap= new HashMap<>();
                                         postMap.put("image_url",downloadUrl);
                                         postMap.put("desc",desc);
                                         postMap.put("user_id",current_user_id);
                                         postMap.put("timestamp",FieldValue.serverTimestamp());
                                       final String department=getIntent().getStringExtra(DEPARTMENTPOST);
                                        final int position= getIntent().getIntExtra(ITEMPOSITION,0);
                                         firebaseFirestore.collection(department).add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                             @Override
                                             public void onComplete(@NonNull Task<DocumentReference> task) {
                                                 if(task.isSuccessful()){
                                                     Toast.makeText(NewPostActivity.this,"Post Added!",Toast.LENGTH_LONG).show();
                                                     Intent mainIntent= new Intent(DepartmentsActivity.newIntent(NewPostActivity.this,department,position,null));
                                                     startActivity(mainIntent);
                                                     finish();}
                                                 else{

                                                 }
                                                 newPostProgress.setVisibility(View.INVISIBLE);
                                                 newPostBtn.setEnabled(true);
                                             }
                                         });


                             }else{
                                 newPostProgress.setVisibility(View.INVISIBLE);
                                 newPostBtn.setEnabled(true);
                             }
                         }
                     }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress =(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            if(progress==0){
                                newPostProgress.setProgress(20);
                            }else {
                            newPostProgress.setProgress((int) progress);}

                        }
                    });;
                }}else{         Map<String,Object> postMap= new HashMap<>();
                    postMap.put("image_url",null);
                    postMap.put("desc",desc);
                    postMap.put("user_id",current_user_id);
                    postMap.put("timestamp",FieldValue.serverTimestamp());
                    final String department=getIntent().getStringExtra(DEPARTMENTPOST);
                    final int position= getIntent().getIntExtra(ITEMPOSITION,0);
                    firebaseFirestore.collection(department).add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(NewPostActivity.this,"Post Added!",Toast.LENGTH_LONG).show();
                                Intent mainIntent= new Intent(DepartmentsActivity.newIntent(NewPostActivity.this,department,position,null));
                                startActivity(mainIntent);
                                finish();}
                            else{

                            }
                            newPostProgress.setVisibility(View.INVISIBLE);
                            newPostBtn.setEnabled(true);
                        }
                    });}

            }
        });
    }
    private Uri getImageUri(Context inContext, Bitmap selectedImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),selectedImage,"Title ",null);

        if(path!=null)
        {
            Uri imageUrii= Uri.parse(path);
            return imageUrii;
        }
        return null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                postImageUri= result.getUri();
                newPostImage.setImageURI(postImageUri);

            }else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error= result.getError();
                Toast.makeText(NewPostActivity.this,"error:"+error,Toast.LENGTH_LONG).show();
            }
        }
    }

}
