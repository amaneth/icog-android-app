package com.example.amankassahun;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepartmentsFragment extends Fragment {

private RecyclerView blog_list_view;
private List<BlogPost> blog_list;
private List<User> user_list;
    private int i;
private BlogRecyclerAdapter blogRecyclerAdapter;
private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad= true;
    private ProgressBar mProgressBar;
    private  static String departmentName;
    private Set<String> authority;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton addPostBtn;
    private String departmentAdmin;
    private static int itemid;
    private boolean allowRefresh=true;
    private boolean fixed;
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
        if(departmentName==null){
            departmentName="Makers";
        }
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
        mDrawerLayout=  view.findViewById(R.id.drawer);
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
        fixed = QueryPreferences.getHiruy(getActivity());
   if(fixed){
        authority=QueryPreferences.getAuthority(getActivity());}
        else{
       authority=QueryPreferences.getAuthority2(getActivity());
   }
   Log.d("kassahun","authority=="+authority+fixed);
        if(authority.contains(departmentAdmin)||authority.contains("main admin")){
            addPostBtn.setVisibility(View.VISIBLE);
        }
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(NewPostActivity.newIntentPost(getActivity(),departmentName,itemid));

            }
        });
        if (firebaseAuth.getCurrentUser() != null){
            firebaseFirestore = FirebaseFirestore.getInstance();
            blog_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean reachedBottom= !recyclerView.canScrollVertically(-1);
                    if(reachedBottom){
                       loadMorePosts();
                    }
                }
            });
            Log.d("kassahun",""+isFirstPageFirstLoad);
            Log.d("natbaro",QueryPreferences.getDept(getActivity())+"ff");
            String privacyy=QueryPreferences.getDept(getActivity());
            Query firstQuery= firebaseFirestore.collection(departmentName).orderBy("timestamp", Query.Direction.DESCENDING);
        firstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(final QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(documentSnapshots!=null){
               if(!documentSnapshots.isEmpty()){
                   if(isFirstPageFirstLoad){
                lastVisible= documentSnapshots.getDocuments().get(documentSnapshots.size()-1);
                   blog_list.clear();
                   user_list.clear();}

                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String bloPostId= doc.getDocument().getId();
                        final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(bloPostId,departmentName);

                       String blogUserId= doc.getDocument().getString("user_id");
firebaseFirestore.collection("Users").document(blogUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
if(task.isSuccessful()){
    User user = task.getResult().toObject(User.class);
    Log.d("kassahun","before"+isFirstPageFirstLoad+i);
    if(isFirstPageFirstLoad){
        user_list.add(user);
        blog_list.add(blogPost);}
    else{
        user_list.add(0,user);
        blog_list.add(0,blogPost);
        Log.d("kassahun","v"+i);
    }
    Log.d("kassahun","a"+i+""+documentSnapshots.getDocumentChanges().size());

    i++;
    if(documentSnapshots.getDocumentChanges().size()==i){
        isFirstPageFirstLoad=false;
    }
    blogRecyclerAdapter.notifyDataSetChanged();
    mProgressBar.setVisibility(View.INVISIBLE);
}
        Log.d("kassahun","b"+i);

    }
});
                        Log.d("kassahun","c"+i);


                    }
                    Log.d("kassahun","d"+i);

                }
                   Log.d("kassahun","e"+i);

              }
                    Log.d("kassahun","f"+i);}
            }
        });
            Log.d("kassahun",""+i);

    } // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        blogRecyclerAdapter.notifyDataSetChanged();


    }



    public void loadMorePosts(){
        String privac=QueryPreferences.getDept(getActivity());
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
                                    blogRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        });

                    }
                }
            }}
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
