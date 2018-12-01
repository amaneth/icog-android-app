package com.example.amankassahun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class NotifyActivity extends SingleFragmentActivity {

    private static final String EXTRA_URL_TO_THE_FRAGMENT =
            "com.example.amankassahun.fragmenturi";
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
        Log.d("noty","createFragment"+urlReteived);
        return NotificationFragmentGallery.newInstance(urlReteived,false);
    }

   }
