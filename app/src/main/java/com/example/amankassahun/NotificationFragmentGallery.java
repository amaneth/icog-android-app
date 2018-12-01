package com.example.amankassahun;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * Created by Aman on 5/5/2018.
 */

public class NotificationFragmentGallery extends VisibleFragment {
    private static final String TAG = "PhotoGalleryFragment";
    private WebsiteFitcher mWebsiteFitcher;
    private RecyclerView mPhotoRecyclerView;
    VerticalRecyclerViewFastScroller fastScroller;
    private List<NotificationGallery> mItems = new ArrayList<>();
    private static String query;
    Activity activity;

    private String current_user_id= UUID.randomUUID().toString();
    private FirebaseUser current_user;
    private static String strTextUrl;
    private static boolean marginNeed;
    private static boolean forSearch=true;
    private static final String EXTRA_URL_TO_THE_FRAGMENT =
            "com.example.amankassahun.fragmenturi";
   private ProgressBar mProgressBar;
    Handler responseHandlerOfWebsite = new Handler();
    Integer count=1;
    private FirebaseFirestore firebaseFirestore;

    public static NotificationFragmentGallery newInstance(String urlRet,boolean margin) {
strTextUrl=urlRet;
        marginNeed=margin;
        return new NotificationFragmentGallery();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Log.d("hir",strTextUrl+"ff");
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Log.d("hopy","why1");

        PollService.setServiceAlarm(getActivity(), true);
        current_user=FirebaseAuth.getInstance().getCurrentUser();
        if(current_user!=null){
         current_user_id= current_user.getUid();}

        firebaseFirestore= FirebaseFirestore.getInstance();


        Log.i(TAG, "Background thread started");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification_gallery, container, false);
        RecyclerView.LayoutParams params=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        Resources r= getActivity().getResources();
        int px;
        if(marginNeed){
       px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,96,r.getDisplayMetrics());}
        else {
            px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,6,r.getDisplayMetrics());
        }
        params.setMargins(0,px,0,0);
        v.setLayoutParams(params);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.notification_recycler_view);
       mProgressBar= (ProgressBar) v.findViewById(R.id.website_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
      fastScroller = (VerticalRecyclerViewFastScroller) v.findViewById(R.id.fast_scrollerinweb);

        // Connect the recycler to the scroller (to let the scroller scroll the list)
        fastScroller.setRecyclerView(mPhotoRecyclerView);

        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)

        Log.d("ethiopia","habesha");
        count=1;
        updateItems();
        mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        mPhotoRecyclerView.addOnScrollListener(fastScroller.getOnScrollListener());
        return v;
    }

    private class FetchItemsTask extends AsyncTask<Integer,Integer,List<NotificationGallery>> {
        private static final String TAG="uog";
        private String mQuery;
        private boolean forSearchCopy;
        public FetchItemsTask(String query,boolean forSearch) {
            mQuery = null;
            forSearchCopy=forSearch;
        }
        @Override
        protected List<NotificationGallery> doInBackground(Integer... params) {








            if (mQuery == null) {
                Log.d("ethiopia","xx"+strTextUrl);
                return new WebsiteFitcher().fetchRecentPhotos(strTextUrl,getActivity());
            } else  {
                return new WebsiteFitcher().searchPhotos(mQuery,strTextUrl);
            }


        }@Override
        protected void onPreExecute(){

        }
        @Override
        protected void onPostExecute(List<NotificationGallery> items) {


            mItems = items;
            boolean smart= mItems==null||mItems.toString().equals("[]");
            Log.d("smart","mItems= "+mItems+smart);

            if(!smart){
                mProgressBar.setVisibility(View.INVISIBLE);
                setupAdapter();

            }
            else {
                setupAdapter();
                updateItems();
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    @Override
    public void  onDestroyView() {
        super.onDestroyView();

    }
    public void showNoConnectionDialog() {

if(isAdded()){
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setMessage("no connection");
        builder.setTitle("Failed");
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               setupAdapter();
               updateItems();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                return;
            }
        });

        builder.show();}
    }
    @Override
    public void onAttach(Activity activityd){

        super.onAttach(activityd);
        activity = activityd;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
menuInflater.inflate(R.menu.main_menu3, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "QueryTextSubmit: " + s);
                query=s;
                forSearch=true;
                updateItems();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "QueryTextChange: " + s);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAdded()){
                String query = QueryPreferences.getStoredQuery(getActivity());}
                searchView.setQuery(query, false);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
                getActivity().invalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateItems() {




        new FetchItemsTask(query,forSearch).execute();


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Background thread destroyed");
    }

    private void setupAdapter() {
        Log.d("smart","before is added");
        if (isAdded()) {
            Log.d("smart","inside is added"+mItems);
            mPhotoRecyclerView.setAdapter(new NotificationAdapter(mItems));
        }
    }
    private class NotificationHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        private View mView;
        private ImageView mItemImageView;
        private NotificationGallery mNotificationGallery;
        private TextView mTitleTextView;
        private TextView mReadMoreTextView;
        private TextView mContentTextView;
        private TextView mContentTextView2;
        private TextView mContentTextVacancy;
        private TextView mSubTitleVacancy;
        private TextView mTitleTextVacancy;
        private TextView mTitleContentInternship;
        private TextView mTitleTextNophoto;
        private Button mButtonOfIntern;
        private ImageView blogLikeBtn;
        private ImageView mCommentsButton;
        private TextView blogLikeCount;
        private TextView blogCommentCount;
        public NotificationHolder(View itemView) {
            super(itemView);
            mButtonOfIntern=(Button) itemView.findViewById(R.id.buttonOfintern);
           mTitleTextView = (TextView) itemView.findViewById(R.id.item_txt_view);
            mReadMoreTextView = (TextView) itemView.findViewById(R.id.read_more);
            mItemImageView = (ImageView) itemView.findViewById(R.id.item_image_view);
            mContentTextView= (TextView) itemView.findViewById(R.id.item_content_view);
            mContentTextView2= (TextView) itemView.findViewById(R.id.item_content_view2);
            mContentTextVacancy= (TextView) itemView.findViewById(R.id.item_content_vacancy);
            mTitleTextVacancy= (TextView) itemView.findViewById(R.id.item_title_vacancy);
            mTitleContentInternship= (TextView) itemView.findViewById(R.id.item_content_internship);
            mSubTitleVacancy= (TextView) itemView.findViewById(R.id.item_subtitle_vacancy);
            mTitleTextNophoto= (TextView) itemView.findViewById(R.id.item_title_nophoto);
            blogLikeBtn= itemView.findViewById(R.id.action_like_of_Web);
            mCommentsButton= itemView.findViewById(R.id.action_comment_of_web);
            blogLikeCount= itemView.findViewById(R.id.action_like_txt_of_web);
            blogCommentCount=itemView.findViewById(R.id.action_txt_comments_of_web);


        }

        public void bindGalleryItem(final NotificationGallery item, int n) {
Log.d("smart","gonna excute"+item);
            if(n==0) {
                Log.d("smart","gonna excute"+n+n+n);
                mItemImageView.setOnClickListener(this);
                mTitleTextView.setText(item.toString());
                mContentTextView.setText(item.getContent());
                mReadMoreTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isAdded()){
                        Intent f= PhotoPageActivity.newIntent(getActivity(), Uri.parse(item.getReadMore()));
                        startActivity(f);}
                    }
                });
                mCommentsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isAdded()){
                        Intent commentIntent= new Intent(getActivity(),CommentsActivity.class);
                        commentIntent.putExtra("blog_post_id",item.getId());
                        startActivity(commentIntent);}
                    }
                });
            }
            else if(n==1){
                mTitleTextNophoto.setText(item.toString());
                mContentTextView2.setText(item.getContent());
            }
            else if(n==2){
                mTitleTextVacancy.setText(item.toString());
                mContentTextVacancy.setText(item.getContent());
                mSubTitleVacancy.setText(item.getSubTitle());
                mPhotoRecyclerView.removeOnScrollListener(fastScroller.getOnScrollListener());
                fastScroller.setVisibility(View.INVISIBLE);
            }
            else if(isAdded()){
                mPhotoRecyclerView.removeOnScrollListener(fastScroller.getOnScrollListener());
                fastScroller.setVisibility(View.INVISIBLE);
                mTitleContentInternship.setText(item.getContent());
                mButtonOfIntern.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent d= PhotoPageActivity.newIntent(getActivity(), Uri.parse(strTextUrl));
                        startActivity(d);
                    }
                });

            }
            mNotificationGallery= item;
        }


        public void bindDrawable(String drawable) {
            Log.d("smart","image attached");
            if(isAdded()){
                RequestOptions requestOptions= new RequestOptions();
                requestOptions=requestOptions.placeholder(R.drawable.holder);
                Glide.with(getActivity()).applyDefaultRequestOptions(requestOptions).load(drawable)
                        .into(mItemImageView);
            }
          // mItemImageView.setImageDrawable(drawable);
        }
        public void updateLikeCount(int count){
            blogLikeCount.setText(count+" Likes");
        }
        public void updateCommentsount(int count){

            blogCommentCount.setText(count+" Comments");
        }
        @Override
        public void onClick(View v) {
            Log.d("tas","abaye");
            if(isAdded()){
            Intent i= PhotoPageActivity.newIntent(getActivity(),mNotificationGallery.getPhotoPageUri());
            startActivity(i);}
        }

    }


    private class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {
        private  final int normalView=0;
        private final int texttOnlyView=1;
        private final int vacancyView=2;
        private final int internshipView=3;
        private List<NotificationGallery> mGalleryItems;
        public NotificationAdapter(List<NotificationGallery> galleryItems) {
            mGalleryItems = galleryItems;
        }
        @Override
        public NotificationHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view;
           // Log.d(TAG,"weye");
            if(viewType==normalView)
            {
                view = inflater.inflate(R.layout.list_notifiy_item, viewGroup, false);
            }
            else if(viewType==texttOnlyView){
                view = inflater.inflate(R.layout.list_notify_item2, viewGroup, false);
            }
            else if(viewType==vacancyView){
                view=inflater.inflate(R.layout.list_notify_item_vacancy,viewGroup,false);
            }
            else{
                view=inflater.inflate(R.layout.list_notify_item_internship,viewGroup,false);
            }

            return new NotificationHolder(view);
        }
        @Override
        public void onBindViewHolder(final NotificationHolder holder, int position) {
            NotificationGallery galleryItem = mGalleryItems.get(position);
            final String blogPostId= galleryItem.getId();
            int itemViewType=getItemViewType(position);
            Log.d(TAG,"this ="+galleryItem.getUrl());
            if(itemViewType==normalView){
            holder.bindGalleryItem(galleryItem,0);}
            else if(itemViewType==texttOnlyView){
                holder.bindGalleryItem(galleryItem,1);
                return;
            }
            else if(itemViewType==vacancyView){
                holder.bindGalleryItem(galleryItem,2);
                return;
            }
            else {
                holder.bindGalleryItem(galleryItem,3);
                return;
            }

            Drawable placeholder = getResources().getDrawable(R.drawable.bill_up_close);
           // notificationHolder.bindDrawable(placeholder);
            Log.d("hopy","wey aman"+galleryItem.getUrl());
            holder.bindDrawable(galleryItem.getUrl());
           // mThumbnailDownloader.queueThumbnail(notificationHolder, galleryItem.getUrl());
            if(isAdded()){
            firebaseFirestore.collection("Posts/"+blogPostId+"/Likes").addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if(!documentSnapshots.isEmpty()){
                        int count = documentSnapshots.size();
                        holder.updateLikeCount(count);
                    }else{
                        holder.updateLikeCount(0);
                    }
                }
            });
                firebaseFirestore.collection("Posts/"+blogPostId+"/Comments").addSnapshotListener((Activity) getActivity(),new EventListener<QuerySnapshot>() {
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
                });}
            if(isAdded()){
            firebaseFirestore.collection("Posts/"+blogPostId+"/Likes").document(current_user_id).addSnapshotListener(getActivity(),new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {

                       if(isAdded())
                        Glide.with(getActivity()).load(R.mipmap.action_like_accent).into(holder.blogLikeBtn);
                    } else {
                        if(isAdded())
                        Glide.with(getActivity()).load(R.mipmap.action_like_grayy).into(holder.blogLikeBtn);

                    }




                }
            });}

            //Likes feature
            if(itemViewType==normalView){
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
                                    Toast.makeText(getActivity(),"aman!",Toast.LENGTH_SHORT).show();
                                }}
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            if(isNetworkAvailableAndConnected()){
                                Map<String, Object> likesMap= new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());

                                firebaseFirestore.collection("Posts/"+blogPostId+"/Likes").document(current_user_id).set(likesMap);
                            }else{
                                Toast.makeText(getActivity(),"check your connection please!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            });
        }}
        private boolean isNetworkAvailableAndConnected() {
            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
            boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
            boolean isNetworkConnected = isNetworkAvailable &&
                    cm.getActiveNetworkInfo().isConnected();
            return isNetworkConnected;
        }
        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
        @Override
        public int getItemViewType(int position)
        {
            NotificationGallery galleryItem = mGalleryItems.get(position);

if(galleryItem.getTitle()==null&&galleryItem.getSubTitle()==null){
    return internshipView;
}else if(galleryItem.getSubTitle()==null&&galleryItem.getUrl().equals("")) {
            return texttOnlyView;
        }
        else if(galleryItem.getSubTitle()!=null){
    return vacancyView;
}
else {
    return normalView;
}

        }
    }

    @Override
    public void onResume() {
        super.onResume();
      NotificationAdapter notificationAdapter=new NotificationAdapter(mItems);

      mPhotoRecyclerView.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
        Log.d("abiy","onResume() called"+mItems.size());
    }
}
