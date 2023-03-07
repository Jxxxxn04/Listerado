package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomepageActivity extends AppCompatActivity {

    LinearLayout NAV_homepage_goToMyProfileLayout, NAV_homepage_goToMyLists;
    ImageView appIcon, navbarProfileImageView;
    Intent switchToAccountIntent, switchToMyListsIntent;
    SwipeRefreshLayout pullToRefresh;
    RelativeLayout obst, gemuese, fleisch, fisch, milchprodukte, suessigkeiten, getraenke, gewuerze, gebaeck;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        //Initialize the needed UI elements from the xml file
        NAV_homepage_goToMyProfileLayout = findViewById(R.id.homepage_navigation_goToMyProfile);
        NAV_homepage_goToMyLists = findViewById(R.id.homepage_navigation_goToMyList);
        appIcon = findViewById(R.id.appIcon);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        navbarProfileImageView = findViewById(R.id.homepage_navbar_ProfileImageView);
        obst = findViewById(R.id.homepage_category_obst);
        gemuese = findViewById(R.id.homepage_category_gemuese);
        fleisch = findViewById(R.id.homepage_category_fleisch);
        fisch = findViewById(R.id.homepage_category_fisch);
        milchprodukte = findViewById(R.id.homepage_category_milchprodukte);
        suessigkeiten = findViewById(R.id.homepage_category_sueßigkeiten);
        getraenke = findViewById(R.id.homepage_category_getraenke);
        gewuerze = findViewById(R.id.homepage_category_gewuerze);
        gebaeck = findViewById(R.id.homepage_category_gebaeck);
        ImageManager imageManager = new ImageManager(HomepageActivity.this, navbarProfileImageView);
        imageManager.refreshImageViewFromSharedPreferences();
        imageManager.refreshImage();


        //Set the Intents
        switchToAccountIntent = new Intent(this, AccountActivity.class);
        switchToMyListsIntent = new Intent(this, MyListsActivity.class);


        obst.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    obst.setBackgroundResource(R.drawable.category_background_obst);
                    isClicked = false;
                }   else {
                    obst.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });


        gemuese.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    gemuese.setBackgroundResource(R.drawable.category_background_gemuese);
                    isClicked = false;
                }   else {
                    gemuese.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        fleisch.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    fleisch.setBackgroundResource(R.drawable.category_background_fleisch);
                    isClicked = false;
                }   else {
                    fleisch.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        fisch.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    fisch.setBackgroundResource(R.drawable.category_background_fisch);
                    isClicked = false;
                }   else {
                    fisch.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        getraenke.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    getraenke.setBackgroundResource(R.drawable.category_background_getraenke);
                    isClicked = false;
                }   else {
                    getraenke.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        gewuerze.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    gewuerze.setBackgroundResource(R.drawable.category_background_gewuerze);
                    isClicked = false;
                }   else {
                    gewuerze.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        gebaeck.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    gebaeck.setBackgroundResource(R.drawable.category_background_gebaeck);
                    isClicked = false;
                }   else {
                    gebaeck.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        milchprodukte.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    milchprodukte.setBackgroundResource(R.drawable.category_background_milchprodukte);
                    isClicked = false;
                }   else {
                    milchprodukte.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        suessigkeiten.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    suessigkeiten.setBackgroundResource(R.drawable.category_background_suessigkeiten);
                    isClicked = false;
                }   else {
                    suessigkeiten.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });



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