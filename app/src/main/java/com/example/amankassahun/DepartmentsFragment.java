package com.example.amankassahun;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okio.Source;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepartmentsFragment extends Fragment {

private RecyclerView blog_list_view;
private List<BlogPost> blog_list;
private List<User> user_list;
    //private int i=0;
    private int j=0;
    private boolean braeaking=false;
private BlogRecyclerAdapter blogRecyclerAdapter;
private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad= true;
    private ProgressBar mProgressBar;
    private  static String departmentName;
    private Set<String> authority;
    private FloatingActionButton addPostBtn;
    private String departmentAdmin;
    private static int itemid;
    public AlertDialog myDialog;
    public DepartmentsFragment() {
        // Required empty public constructor
    }
    public static DepartmentsFragment newInstance(String department,int item) {
        departmentName=department;
        itemid=item;
        return new DepartmentsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_departments, container, false);

        blog_list = new ArrayList<>();
        user_list= new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(departmentName==null){
            departmentName="Makers";
        }
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        firebaseFirestore.setFirestoreSettings(settings);
       // firebaseFirestore.setFirestoreSettings(settings);
        addPostBtn =  view.findViewById(R.id.add_post_btn);
        blog_list_view = view.findViewById(R.id.blog_list_view);
        blog_list_view.setHasFixedSize(true);
        firebaseAuth = FirebaseAuth.getInstance();
        blogRecyclerAdapter = new BlogRecyclerAdapter(blog_list,user_list);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        blog_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_list_view.setAdapter(blogRecyclerAdapter);
        VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) view.findViewById(R.id.fast_scroller);

        // Connect the recycler to the scroller (to let the scroller scroll the list)
        fastScroller.setRecyclerView(blog_list_view);

        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
        blog_list_view.addOnScrollListener(fastScroller.getOnScrollListener());
        mProgressBar= view.findViewById(R.id.home_fragment_progress);
        switch (departmentName){
            case "Makers":
                departmentAdmin="makers admin";
                break;
            case "ACC":
                departmentAdmin="acc admin";
                break;
            case "SolveIt":
                departmentAdmin="solvit admin";
                break;
            case "Design in Ethiopia":
                departmentAdmin="die admin";
                break;
            case "icoggers":
                departmentAdmin="main admin";
                break;
            default:
                departmentAdmin="main admin";
                break;

        }

        authority=QueryPreferences.getAuthority(getActivity());

        if(authority.contains(departmentAdmin)||authority.contains("main admin")){
            addPostBtn.setVisibility(View.VISIBLE);
        }
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(NewPostActivity.newIntentPost(getActivity(),departmentName,itemid));

            }
        });
        blog_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Boolean reachedBottom= !recyclerView.canScrollVertically(1);
                if(reachedBottom){
                    loadMorePosts();
                }
            }
        });
       Retry(); // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        blogRecyclerAdapter.notifyDataSetChanged();


    }



    public void loadMorePosts(){
        Toast.makeText(getActivity(),"Loading...",Toast.LENGTH_SHORT).show();
       // mProgressBar.setVisibility(View.VISIBLE);
        Query nextQuery= firebaseFirestore.collection(departmentName).orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(3);
        nextQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(documentSnapshots!=null){
                if(!documentSnapshots.isEmpty()){
                lastVisible= documentSnapshots.getDocuments().get(documentSnapshots.size()-1);
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String blogPostId= doc.getDocument().getId();
                        final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId,departmentName);
                        String blogUserId= doc.getDocument().getString("user_id");
                        firebaseFirestore.collection("Users").document(blogUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    User user = task.getResult().toObject(User.class);
                                        user_list.add(user);
                                        blog_list.add(blogPost);
                                   // Toast.makeText(getActivity(),""+blog_list.size(),Toast.LENGTH_SHORT).show();
                                    blogRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        });

                    }
                }
            }}
            }
        });
       // mProgressBar.setVisibility(View.INVISIBLE);
    }
    private void showDialogg(){

        //startActivity(new Intent(getActivity(),HomeActivity.class));
        if(myDialog!=null&&myDialog.isShowing()){
            return;}
        else  {
            if(isAdded()){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Check your internet connection please!")
                .setCancelable(false)
                .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       Retry();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        myDialog=builder.create();
        myDialog.show();}

    }}
    private void Retry(){
        if (firebaseAuth.getCurrentUser() != null){

            /*
                     if(documentSnapshots!=null){
                        BlogRecyclerAdapter.DrAbiyAhmed=!documentSnapshots.getMetadata().hasPendingWrites();
                        if(!documentSnapshots.isEmpty()){
                            if(isFirstPageFirstLoad){
                                lastVisible= documentSnapshots.getDocuments().get(documentSnapshots.size()-1);
                                //blog_list.clear();
                                //user_list.clear();
                            }

                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                               if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String bloPostId= doc.getDocument().getId();
                                    final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(bloPostId,departmentName);
                                    //Log.d("aman","size="+",brakinge="+braeaking);

                                    String blogUserId= doc.getDocument().getString("user_id");
                                    firebaseFirestore.collection("Users").document(blogUserId).get(com.google.firebase.firestore.Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                User user = task.getResult().toObject(User.class);
                                                if(isFirstPageFirstLoad){
                                                    user_list.add(user);
                                                    blog_list.add(blogPost);
                                                    // Toast.makeText(getActivity(),""+blog_list.size(),Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    user_list.add(0,user);
                                                    blog_list.add(0,blogPost);
                                                    //Toast.makeText(getActivity(),""+blog_list.size(),Toast.LENGTH_SHORT).show();

                                                }

                                                // i++;
                                                //if(documentSnapshots.getDocumentChanges().size()==i){
                                                isFirstPageFirstLoad=false;
                                                //}
                                                blogRecyclerAdapter.notifyDataSetChanged();
                                                mProgressBar.setVisibility(View.INVISIBLE);
                                                braeaking=false;
                                               // Log.d("aman","braking"+braeaking);
                                            }else  {
                                               // Log.d("aman","size=c"+j);
                                                String errorMessage= task.getException().getMessage();
                                                Toast.makeText(getActivity(),"Error "+errorMessage,Toast.LENGTH_LONG).show();
                                               // Log.d("aman",errorMessage);
                                                //Log.d("aman",""+errorMessage.equals("Failed to get document because the client is offline.")) ;
                                               /* if (errorMessage.equals("Failed to get document because the client is offline.")) {
                                                    if(j==2){
                                                    if(isNetworkAvailableAndConnected()){
                                                        Retry();
                                                        j=0;

                                                    }else{
                                                        showDialogg();
                                                        j=0;
                                                    }}else{
                                                        j++;
                                                    }


                                                }
        }

    }
});


        }
        //i++;


        }

        }
        }
            */
            Query firstQuery= firebaseFirestore.collection(departmentName).orderBy("timestamp", Query.Direction.DESCENDING).limit(3);
            firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if(documentSnapshots!=null){
                        BlogRecyclerAdapter.DrAbiyAhmed=!documentSnapshots.getMetadata().hasPendingWrites();
                        if(!documentSnapshots.isEmpty()){
                            if(isFirstPageFirstLoad){
                                lastVisible= documentSnapshots.getDocuments().get(documentSnapshots.size()-1);
                                blog_list.clear();
                                user_list.clear();
                            }

                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String bloPostId= doc.getDocument().getId();
                                    final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(bloPostId,departmentName);
                                    //Log.d("aman","size="+",brakinge="+braeaking);

                                    String blogUserId= doc.getDocument().getString("user_id");
                                    firebaseFirestore.collection("Users").document(blogUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                User user = task.getResult().toObject(User.class);
                                                if(isFirstPageFirstLoad){
                                                    user_list.add(user);
                                                    blog_list.add(blogPost);
                                                    // Toast.makeText(getActivity(),""+blog_list.size(),Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    user_list.add(0,user);
                                                    blog_list.add(0,blogPost);
                                                    //Toast.makeText(getActivity(),""+blog_list.size(),Toast.LENGTH_SHORT).show();

                                                }

                                                // i++;
                                                //if(documentSnapshots.getDocumentChanges().size()==i){
                                                isFirstPageFirstLoad=false;
                                                //}
                                                blogRecyclerAdapter.notifyDataSetChanged();
                                                mProgressBar.setVisibility(View.INVISIBLE);
                                                braeaking=false;
                                                // Log.d("aman","braking"+braeaking);
                                            }else  {
                                                // Log.d("aman","size=c"+j);
                                                String errorMessage= task.getException().getMessage();
                                             //   if(DepartmentsFragment.this.isVisible()&&isAdded())
                                              //  Toast.makeText(getActivity(),"Error "+errorMessage,Toast.LENGTH_LONG).show();
                                                // Log.d("aman",errorMessage);
                                                //Log.d("aman",""+errorMessage.equals("Failed to get document because the client is offline.")) ;
                                               if (errorMessage.equals("Failed to get document because the client is offline.")) {
                                                    if(j==2){
                                                    if(isNetworkAvailableAndConnected()){
                                                        Retry();
                                                        j=0;

                                                    }else{
                                                        showDialogg();
                                                        j=0;
                                                    }}else{
                                                        j++;
                                                    }


                                                }
        }

    }
});


        }
        //i++;


        }

        }
        }
                }
            });

        }


    }
    private boolean isNetworkAvailableAndConnected() {
        if(isAdded()){
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
    else {
            return false;
        }}

    @Override
    public void onStart() {
        super.onStart();
    }
}
