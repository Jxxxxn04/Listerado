package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MyListsActivity extends AppCompatActivity {

    LinearLayout NAV_myList_goToHomepageLayout, NAV_myList_goTomyAccountLayout;
    Intent switchToAccountIntent, switchToHomepageIntent;
    ImageView navbar_ProfileImageView;
    private List<String> items;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lists);

        NAV_myList_goToHomepageLayout = findViewById(R.id.myList_navigation_goToHomepage);
        NAV_myList_goTomyAccountLayout= findViewById(R.id.myList_navigation_goToMyProfile);
        navbar_ProfileImageView = findViewById(R.id.myLists_navbar_ProfilImageView);
        ImageManager imageManager = new ImageManager(MyListsActivity.this, navbar_ProfileImageView);
        imageManager.refreshImageViewFromSharedPreferences();
        imageManager.refreshImage();



        switchToAccountIntent = new Intent(this, AccountActivity.class);
        switchToHomepageIntent = new Intent(this, HomepageActivity.class);



        // Datenquelle für die Liste
        items = new ArrayList<>();
        items.add("Element 1");
        items.add("Element 2");
        items.add("Element 3");
        items.add("Element 4");
        items.add("Element 5");
        items.add("Element 6");
        items.add("Element 7");
        items.add("Element 8");
        items.add("Element 9");





        NAV_myList_goToHomepageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(switchToHomepageIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        NAV_myList_goTomyAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(switchToAccountIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });





        // ArrayAdapter initialisieren und an ListView binden
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_lists_template, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.my_lists_template, parent, false);
                }
                TextView textview = (TextView) view.findViewById(R.id.listName);
                textview.setText(items.get(position));
                return view;
            }
        };
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }



    public void onBackPressed() {
        startActivity(new Intent(MyListsActivity.this, HomepageActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


}

