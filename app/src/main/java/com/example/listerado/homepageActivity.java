package com.example.listerado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class homepageActivity extends AppCompatActivity {

    Integer countIconClicker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);

        LinearLayout NAV_homepage_goToMyProfileLayout = findViewById(R.id.homepage_navigation_goToMyProfile);
        LinearLayout NAV_homepage_goToMyLists = findViewById(R.id.homepage_navigation_goToMyList);

        ImageView appIcon = findViewById(R.id.appIcon);

        Intent dontTouchIntent = new Intent(this, dontTouch.class);
        Intent switchToAccountIntent = new Intent(this, accountActivity.class);
        Intent test = new Intent(this, loginActivity.class);






        NAV_homepage_goToMyLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(test);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });




            NAV_homepage_goToMyProfileLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(switchToAccountIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });




        appIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countIconClicker++;


                if (countIconClicker == 88 || countIconClicker == 69) {
                    new CountDownTimer(5000, 1000) {
                        // 5000 Millisekunden (5 Sekunden), CountDownInterval = 1000 Millisekunden (1 Sekunde)
                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            if (countIconClicker == 88 || countIconClicker == 69) {
                                startActivity(dontTouchIntent);
                            } else {
                                countIconClicker = 0;
                            }

                        }
                    }.start();
                }
            }
        });



        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                pullToRefresh.setRefreshing(false);

            }
        });
    }
}