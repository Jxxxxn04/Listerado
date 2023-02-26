package com.example.listerado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HomepageActivity extends AppCompatActivity {

    LinearLayout NAV_homepage_goToMyProfileLayout, NAV_homepage_goToMyLists;
    ImageView appIcon;
    Intent switchToAccountIntent, switchToMyListsIntent;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        //Initialize the needed UI elements from the xml file
         NAV_homepage_goToMyProfileLayout = findViewById(R.id.homepage_navigation_goToMyProfile);
         NAV_homepage_goToMyLists = findViewById(R.id.homepage_navigation_goToMyList);
         appIcon = findViewById(R.id.appIcon);
         pullToRefresh = findViewById(R.id.pullToRefresh);


         //Set the Intents
         switchToAccountIntent = new Intent(this, AccountActivity.class);
         switchToMyListsIntent = new Intent(this, MyListsActivity.class);



            //Switch Activity to accountActivity
            NAV_homepage_goToMyProfileLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(switchToAccountIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            });

            //Switch Activity to homepageActivity
            NAV_homepage_goToMyLists.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(switchToMyListsIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            });


            //Refresh the Featured Products
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    pullToRefresh.setRefreshing(false);

                }
            });
    }
}