package com.example.amankassahun;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;


/**
 * Created by Aman on 5/22/2018.
 */

public class BlogService extends IntentService {
    private static final String TAG = "BlogService";
    private static String  bloPostId,title,desc;
    private FirebaseFirestore firebaseFirestore;
    public BlogService() {
        super(TAG);
    }

    public static Intent newIntent(Context context)
    {
        Intent serviceIntent= new Intent(context.getApplicationContext(),BlogService.class);

        return  serviceIntent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
firebaseFirestore= FirebaseFirestore.getInstance();
        String privacy= QueryPreferences.getDept(getApplicationContext());
        Query makersQuery= firebaseFirestore.collection("Makers").orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(3);
        makersQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(documentSnapshots!=null){
                    if(!documentSnapshots.isEmpty()){
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                 bloPostId= doc.getDocument().getId();
                                boolean cant=bloPostId.equals(QueryPreferences.getLatestPostmakersKey(getApplicationContext()));


                                if(!cant){
                                     String imageUrl=doc.getDocument().getString("image_url");
                                     desc=doc.getDocument().getString("desc");
                                  new FetchItemsTask().execute(imageUrl);
                                    QueryPreferences.setLatestPostmakerskey(getApplicationContext(),bloPostId );



                                }




                            }
                            break;
                        }
                      }}
            }
        });

        Query accQuery= firebaseFirestore.collection("ACC").orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(3);
        accQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(documentSnapshots!=null){
                    if(!documentSnapshots.isEmpty()){
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                bloPostId= doc.getDocument().getId();
                                boolean cant=bloPostId.equals(QueryPreferences.getLatestPostaccKey(getApplicationContext()));


                                if(!cant){
                                    String imageUrl=doc.getDocument().getString("image_url");
                                    desc=doc.getDocument().getString("desc");
                                    new FetchItemsTask().execute(imageUrl);
                                    QueryPreferences.setLatestPostacckey(getApplicationContext(),bloPostId );



                                }




                            }
                            break;
                        }
                    }}
            }
        });
        Query solveQuery= firebaseFirestore.collection("SolveIt").orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(3);
        solveQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(documentSnapshots!=null){
                    if(!documentSnapshots.isEmpty()){
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                bloPostId= doc.getDocument().getId();
                                boolean cant=bloPostId.equals(QueryPreferences.getLatestPostsolveKey(getApplicationContext()));


                                if(!cant){
                                    String imageUrl=doc.getDocument().getString("image_url");
                                    desc=doc.getDocument().getString("desc");
                                    new FetchItemsTask().execute(imageUrl);
                                    QueryPreferences.setLatestPostsolveKey(getApplicationContext(),bloPostId );



                                }




                            }
                            break;
                        }
                    }}
            }
        });
        Query dieQuery= firebaseFirestore.collection("Design in Ethiopia").orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(3);
        dieQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(documentSnapshots!=null){
                    if(!documentSnapshots.isEmpty()){
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                bloPostId= doc.getDocument().getId();
                                boolean cant=bloPostId.equals(QueryPreferences.getLatestPostdieKey(getApplicationContext()));


                                if(!cant){
                                    String imageUrl=doc.getDocument().getString("image_url");
                                    desc=doc.getDocument().getString("desc");
                                    new FetchItemsTask().execute(imageUrl);
                                    QueryPreferences.setLatestPostdiekey(getApplicationContext(),bloPostId );




                                }




                            }
                            break;
                        }
                    }}
            }
        });
String dept=QueryPreferences.getDept(getApplicationContext());
boolean iCogger=dept.equals("public")||dept.equals("publican");
        if(!iCogger) {
            Query icogQuery = firebaseFirestore.collection("icoggers").orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(3);
            icogQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (documentSnapshots != null) {
                        if (!documentSnapshots.isEmpty()) {
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    bloPostId = doc.getDocument().getId();
                                    boolean cant = bloPostId.equals(QueryPreferences.getLatestPosticogKey(getApplicationContext()));


                                    if (!cant) {
                                        String imageUrl = doc.getDocument().getString("image_url");
                                        desc = doc.getDocument().getString("desc");
                                        new FetchItemsTask().execute(imageUrl);
                                        QueryPreferences.setLatestPosticogKey(getApplicationContext(),bloPostId );

                                    }


                                }
                                break;
                            }
                        }
                    }
                }
            });
        }


}
    private class FetchItemsTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String url= params[0];






                byte[] bitmapBytes = new byte[0];
                try {
                    bitmapBytes = new WebsiteFitcher().getUrlBytes(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final Bitmap bitmap = BitmapFactory
                        .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);




                return bitmap;





        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
                Intent intent1 = new Intent(getApplicationContext(),DepartmentsActivity.class);
                // Uri uri= Uri.parse("http://www.uog.edu.et/wp-content/uploads/2013/03/biniam.mp3");
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent1, 0);
                Notification notification = new NotificationCompat.Builder(getApplicationContext(),"M_CH_ID")
                        .setTicker(title)
                        .setSmallIcon(R.drawable.action_notification)
                        .setLargeIcon(bitmap)


                        .setContentTitle(title)
                        .setContentText(desc)
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .build();

                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(0, notification);


        }
    }}

