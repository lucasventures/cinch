package com.virtualspaces.cinch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.virtualspaces.cinch.Constants;
import com.virtualspaces.cinch.R;

public class SplashActivity extends AppCompatActivity {

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        title = (TextView) findViewById(R.id.splash_title);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/spartan.ttf");


        Handler handler = new Handler();

        AlphaAnimation anim = new AlphaAnimation(0, 1);
        anim.setInterpolator(new LinearOutSlowInInterpolator());
        anim.setDuration(1800);
        anim.start();

        title.setVisibility(View.VISIBLE);
        title.setTypeface(typeface);

        title.setText("cinch");

        title.setAnimation(anim);


        handler.postDelayed(new

                                    Runnable() {
                                        @Override
                                        public void run() {
                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);

                                            if (prefs.getInt(Constants.PIN, 0) == 0) {
                                                Intent intent = new Intent(SplashActivity.this, IntroductionActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(SplashActivity.this, SecurityActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.putExtra("class", 1);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }

                , 2000);


    }


}
