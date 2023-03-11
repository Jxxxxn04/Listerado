package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MyListsActivity extends AppCompatActivity {

    LinearLayout NAV_myList_goToHomepageLayout, NAV_myList_goTomyAccountLayout;
    Intent switchToAccountIntent, switchToHomepageIntent;
    ImageView navbar_ProfileImageView, addListImageView;
    ListView listView;
    protected ArrayList<String> items;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lists);

        NAV_myList_goToHomepageLayout = findViewById(R.id.myList_navigation_goToHomepage);
        NAV_myList_goTomyAccountLayout= findViewById(R.id.myList_navigation_goToMyProfile);
        navbar_ProfileImageView = findViewById(R.id.myLists_navbar_ProfilImageView);
        addListImageView = findViewById(R.id.myLists_add_list_button);
        listView = findViewById(R.id.myLists_listView);
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




        addListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyListsActivity.this);
                View blub = LayoutInflater.from(MyListsActivity.this).inflate(R.layout.template_simple_dialog_popup_window, null);
                builder.setView(blub);

                EditText editText = blub.findViewById(R.id.simplePopup_editTextField);

                builder.setPositiveButton("Senden", (dialogInterface, i) -> {
                    items.add(editText.getText().toString());

                    ListView listView = findViewById(R.id.myLists_listView);
                    MyListAdapter adapter = new MyListAdapter(MyListsActivity.this, items);
                    listView.setAdapter(adapter);
                });

                builder.setNegativeButton("Abbrechen", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

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

        ListView listView = findViewById(R.id.myLists_listView);
        MyListAdapter adapter = new MyListAdapter(this, items);
        listView.setAdapter(adapter);



    }


    public ArrayList<String> getItems() {
        return items;
    }

    public void onBackPressed() {
        startActivity(new Intent(MyListsActivity.this, HomepageActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


}

