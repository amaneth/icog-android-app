package com.example.amankassahun;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Aman on 9/29/2018.
 */

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {
    private  final int blog_list_withphoto=0;
    private final int blog_list_nophoto=1;
    private Set<String> current_user_authority;
    public List<BlogPost> blog_list;
    public List<User> user_list;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String authorityBull;
    private String current_user_id;
    private String departmen;
    public BlogRecyclerAdapter(List<BlogPost> blog_list,List<User> user_list){
        this.blog_list=blog_list;
        this.user_list=user_list;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==blog_list_withphoto){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item,parent,false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_nophoto,parent,false);}

        context=parent.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser current_userr=firebaseAuth.getCurrentUser();
        if(current_userr!=null){
         current_user_id= current_userr.getUid();}

        current_user_authority=QueryPreferences.getAuthority(context);
        return new ViewHolder(view);
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
    public void showAddAdminDialog(final String authority, final String person, final String user_id) {
        View checkBoxView=null;
        authorityBull=authority;
            if(authority.equals("main admin")) {
                 checkBoxView = View.inflate(context, R.layout.activity_admin_choice, null);
                RadioButton admincheckBox = checkBoxView.findViewById(R.id.admin_radio_btn);
                RadioButton acc = checkBoxView.findViewById(R.id.acc_admin);
                RadioButton makers = checkBoxView.findViewById(R.id.makers_admin);
                RadioButton solveIt = checkBoxView.findViewById(R.id.solveit_admin);
                RadioButton die = checkBoxView.findViewById(R.id.die_admin);

                admincheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        authorityBull="main admin";// Save to shared preferences
                    }
                });
                acc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        authorityBull="acc admin";// Save to shared preferences
                    }
                });
                makers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        authorityBull="makers admin"; // Save to shared preferences
                    }
                });
                solveIt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        authorityBull="solvit admin";// Save to shared preferences
                    }
                });
                die.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        authorityBull="die admin";// Save to shared preferences
                    }
                });
            }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to make "+person+" "+authority)
                .setView(checkBoxView)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final DocumentReference docRef=firebaseFirestore.collection("Users").document(user_id);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    if(task.getResult().exists()){
                                        Map<String,Object> updates= new HashMap<>();

                                            updates.put("authority",authorityBull);


                                        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Toast.makeText(context,person+" is "+authorityBull+" admin now",Toast.LENGTH_LONG).show();
                                                notifyDataSetChanged();
                                            }
                                        });




                                    }
                                }
                                else{
                                    String error= task.getException().getMessage();
                                    Toast.makeText(context,"ERROR: "+error,Toast.LENGTH_LONG).show();
                                }

                            }
                        });                         }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();

    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        int itemViewType = getItemViewType(position);
        final String blogPostId= blog_list.get(position).BlogPostId;
        final String departmentof=blog_list.get(position).DEPARTMENTOF;

String desc_data= blog_list.get(position).getDesc();
        holder.setDescText(desc_data);
        if(itemViewType==blog_list_withphoto){
        String image_url= blog_list.get(position).getImage_url();
        holder.setBlogImage(image_url);}
        final String blog_user_id= blog_list.get(position).getUser_id();
        final String department=QueryPreferences.getDept(context);
        boolean amaex=current_user_authority.equals("x")||department.equals("public");

        if(blog_user_id.equals(current_user_id)||!amaex||current_user_authority.contains("main admin")){
            holder.blogDeleteBtn.setEnabled(true);
        }
        String name=null,image=null;

        if(user_list.get(position)!=null){
         name =user_list.get(position).getName();
         image= user_list.get(position).getImage();
     }


        holder.setUserData(name,image);
        Date date= blog_list.get(position).getTimestamp();
if(date!=null){
   long millisecond= date.getTime();
        String dateString = DateFormat.format("MM/dd/yyyy",new Date(millisecond)).toString();
        holder.setTime(dateString);}

        //Get Likes
        firebaseFirestore.collection("Posts/"+blogPostId+"/Likes").addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
               if(documentSnapshots!=null){
                if(!documentSnapshots.isEmpty()){
                    int count = documentSnapshots.size();
                 holder.updateLikeCount(count);
                }else{
                    holder.updateLikeCount(0);
                }}
            }
        });
        firebaseFirestore.collection("Posts/"+blogPostId+"/Comments").addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(documentSnapshots!=null){
                    if(!documentSnapshots.isEmpty()){
                        int count = documentSnapshots.size();
                        holder.updateCommentsount(count);
                    }else{
                        holder.updateCommentsount(0);
                    }}
            }
        });
        firebaseFirestore.collection("Posts/"+blogPostId+"/Likes").document(current_user_id).addSnapshotListener((Activity) context,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot != null){
                    if (documentSnapshot.exists()) {


                        Glide.with(context).load(R.mipmap.action_like_accent).into(holder.blogLikeBtn);
                    } else {

                        Glide.with(context).load(R.mipmap.action_like_grayy).into(holder.blogLikeBtn);

                    }

            }
            }
        });


        //Likes feature
        holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Posts/"+blogPostId+"/Likes").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                if(!task.getResult().exists()){
                    Map<String, Object> likesMap= new HashMap<>();
                    likesMap.put("timestamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("Posts/"+blogPostId+"/Likes").document(current_user_id).set(likesMap);
                }else if(task.getResult().exists()){
                    firebaseFirestore.collection("Posts/"+blogPostId+"/Likes").document(current_user_id).delete();

                }else {
                    Toast.makeText(context,"aman!",Toast.LENGTH_SHORT).show();
                }}
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        if(!isNetworkAvailableAndConnected()){
                        Toast.makeText(context,"check your connection ",Toast.LENGTH_SHORT).show();}
                        else{
                            Map<String, Object> likesMap= new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Posts/"+blogPostId+"/Likes").document(current_user_id).set(likesMap);
                        }

                    }
                });


            }
        });
        holder.blogCommentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent= new Intent(context,CommentsActivity.class);
                commentIntent.putExtra("blog_post_id",blogPostId);
                ((Activity) context).startActivityForResult(commentIntent,1);
            }
        });
        holder.blogCommentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent= new Intent(context,CommentsActivity.class);
                commentIntent.putExtra("blog_post_id",blogPostId);
                ((Activity) context).startActivityForResult(commentIntent,1);
            }
        });
holder.blogDeleteBtn.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to delete this Post!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseFirestore.collection(departmentof).document(blogPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                blog_list.remove(position);
                                user_list.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
        return true;
    }
});

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }
    public void CheckingDelete(final int position, final String blogPostId){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to delete this Post!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseFirestore.collection("Posts").document(blogPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                blog_list.remove(position);
                                user_list.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
     private View mView;
     private TextView descView;
     private TextView usernameView;
      private ImageView blogImageView;
      private ImageView profileImage;
      private TextView  blogDate;
      private ImageView blogLikeBtn;
        private ImageView blogCommentsBtn;
      private TextView  blogLikeCount;
        private TextView blogCommentCount;
        private CardView blogDeleteBtn;
        private ProgressBar progressForDialog;
        public ViewHolder(View itemView) {
            super(itemView);
            mView= itemView;
            blogLikeBtn= (ImageView) mView.findViewById(R.id.blog_like_btn);
            blogCommentsBtn= mView.findViewById(R.id.blog_comments_btn);
            blogDeleteBtn = mView.findViewById(R.id.main_blog);
            blogCommentCount= mView.findViewById(R.id.blog_comments_count);

            // Set the maximum radian the device should rotate to show image's bounds.
            // It should be set between 0 and π/2.
            // The default value is π/9.


             blogImageView =  mView.findViewById(R.id.ad_image);
            // Set GyroscopeObserver for PanoramaImageView.


        }
        public void setDescText(String descText){
           descView= mView.findViewById(R.id.blog_desc);
            descView.setText(descText);

        }

        public void setBlogImage(String downloadUrl){

            RequestOptions requestOptions= new RequestOptions();
            requestOptions=requestOptions.placeholder(R.drawable.holder);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUrl)
                    .into(blogImageView);
        }
        public void setUserData(String userName,String downloadUrl){
            usernameView= mView.findViewById(R.id.blog_user_name);
            profileImage=mView.findViewById(R.id.blog_user_image);

            if(userName!=null) {

                    usernameView.setText(userName);

                RequestOptions placeHolderRequest = new RequestOptions();
                placeHolderRequest = placeHolderRequest.placeholder(R.drawable.holder);
                Glide.with(context).setDefaultRequestOptions(placeHolderRequest).load(downloadUrl).into(profileImage);
            }
            else{
                Glide.with(context).load(R.drawable.holder).into(profileImage);
                usernameView.setText("Guest");
            }

        }

        public void setTime(String date){
blogDate = mView.findViewById(R.id.blog_date);
            blogDate.setText(date);

        }
        public void updateLikeCount(int count){
        blogLikeCount= mView.findViewById(R.id.blog_like_count);
            blogLikeCount.setText(count+" Likes");
        }
        public void updateCommentsount(int count){

            blogCommentCount.setText(count+" Comments");
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(blog_list.get(position).getImage_url()==null){
            return blog_list_nophoto;

        }else {
            return blog_list_withphoto;
        }
    }


}
