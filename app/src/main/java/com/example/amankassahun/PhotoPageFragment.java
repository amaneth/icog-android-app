package com.example.amankassahun;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by Aman on 9/16/2018.
 */

public class PhotoPageFragment extends VisibleFragment {
    private static final String ARG_URI="photo_page_url";
    private Uri mUri;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    public static PhotoPageFragment newInstance(Uri uri){
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        PhotoPageFragment fragment= new PhotoPageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mUri = getArguments().getParcelable(ARG_URI);
    }
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_photo_page,container,false);
        mProgressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        mToolbar= v.findViewById(R.id.web_view_toolbar);
        mProgressBar.setMax(100); // WebChromeClient reports in range 0-100
        mWebView= (WebView) v.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
            public void onReceivedTitle(WebView webView, String title) {
                if(isAdded()){
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                    activity.setSupportActionBar(mToolbar);
                activity.getSupportActionBar().setSubtitle(title);}
            }
        });
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mUri.toString());
        return v;
    }
}
