package com.example.amankassahun;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class LikeUsActivity extends AppCompatActivity {
    private ImageView fakeImageButton;
    private ImageView twittButton;
    private Toolbar likeUsToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.likeus);
        likeUsToolbar = (Toolbar) findViewById(R.id.likeus_toolbar);
        setSupportActionBar(likeUsToolbar);
        getSupportActionBar().setTitle("Like Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fakeImageButton= findViewById(R.id.fake_image_button);
        twittButton= findViewById(R.id.twitt_like_btn);
        fakeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PhotoPageActivity.newIntent(LikeUsActivity.this, Uri.parse("https://m.facebook.com/iCog-Labs")));
            }
        });
        twittButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PhotoPageActivity.newIntent(LikeUsActivity.this, Uri.parse("https://twitter.com/iCog_Labs")));
            }
        });
    }
}
