package com.example.amankassahun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MemberListActivity extends AppCompatActivity {
    private Toolbar memberListToolbar;
    private FirebaseFirestore firebaseFirestore;

    private MembersRecyclerAdapter membersRecyclerAdapter;
    private RecyclerView members_list;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        memberListToolbar= (Toolbar) findViewById(R.id.member_list_toolbar);
        setSupportActionBar(memberListToolbar);
        getSupportActionBar().setTitle("Members");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseFirestore= FirebaseFirestore.getInstance();

        members_list= findViewById(R.id.members_list);
        userList= new ArrayList<>();
        membersRecyclerAdapter= new MembersRecyclerAdapter(userList);
        members_list.setHasFixedSize(true);
        members_list.setLayoutManager(new LinearLayoutManager(this));
        members_list.setAdapter(membersRecyclerAdapter);

        firebaseFirestore.collection("Users").addSnapshotListener(MemberListActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()){
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            //String commentId= doc.getDocument().getId();
                            User users= doc.getDocument().toObject(User.class);
                            userList.add(users);
                            membersRecyclerAdapter.notifyDataSetChanged();



                        }}}}
        });


    }
}
