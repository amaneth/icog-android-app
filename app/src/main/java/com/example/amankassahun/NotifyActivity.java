package com.example.amankassahun;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;


public class NotifyActivity extends SingleFragmentActivity {
    private static final String EXTRA_URL =
            "com.example.amankassahun.uri";

    String urlReteived;
    public static Intent newIntent(Context packageContext,String answerIsTrue){
        Intent intent = new Intent(packageContext,NotifyActivity.class);
        intent.putExtra(EXTRA_URL,answerIsTrue);
        return intent;

    }

    @Override
    protected Fragment createFragment() {
        urlReteived= getIntent().getStringExtra(EXTRA_URL);
        if(urlReteived==null){
            urlReteived="http://www.icog-labs.com/news/";
        }
        return NotificationFragmentGallery.newInstance(urlReteived,false);
    }

   }
