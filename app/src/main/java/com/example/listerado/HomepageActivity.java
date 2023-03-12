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
            String id = "1";
            Boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                }   else    {
                    obst.setBackgroundResource(R.drawable.list_background);

                    //TODO Neue Random Produkte werden angezeigt (idee)

                    isClicked = true;
                }

            }
        });


        gemuese.setOnClickListener(new View.OnClickListener() {
            String id = "2";
            Boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                }   else    {
                    gemuese.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        fleisch.setOnClickListener(new View.OnClickListener() {
            String id = "3";
            Boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                }   else    {
                    fleisch.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        fisch.setOnClickListener(new View.OnClickListener() {
            String id = "4";
            Boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                }   else    {
                    fisch.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        getraenke.setOnClickListener(new View.OnClickListener() {
            String id = "7";
            Boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                }   else    {
                    getraenke.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        gewuerze.setOnClickListener(new View.OnClickListener() {
            String id = "8";
            Boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                }   else    {
                    gewuerze.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        gebaeck.setOnClickListener(new View.OnClickListener() {
            String id = "9";
            Boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                }   else    {
                    gebaeck.setBackgroundResource(R.drawable.list_background);
                    isClicked = true;
                }
            }
        });

        milchprodukte.setOnClickListener(new View.OnClickListener() {
            String id = "5";
            Boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                }   else    {
                    milchprodukte.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                }
            }
        });

        suessigkeiten.setOnClickListener(new View.OnClickListener() {
            String id = "6";
            Boolean isClicked = true;
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                }   else    {
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



    void handleSelection(View selectedView) {
        View[] views = {obst, gemuese, fleisch, fisch, getraenke, gewuerze, gebaeck, milchprodukte, suessigkeiten};
        for (View view : views) {
            if (view == selectedView) {
                view.setBackgroundResource(getBackgroundResId(view));
            } else {
                view.setBackgroundResource(R.drawable.list_background);
            }
        }
    }

    private int getBackgroundResId(View view) {
        switch (view.getId()) {
            case R.id.homepage_category_obst:
                return R.drawable.category_background_obst;
            case R.id.homepage_category_gemuese:
                return R.drawable.category_background_gemuese;
            case R.id.homepage_category_fleisch:
                return R.drawable.category_background_fleisch;
            case R.id.homepage_category_fisch:
                return R.drawable.category_background_fisch;
            case R.id.homepage_category_getraenke:
                return R.drawable.category_background_getraenke;
            case R.id.homepage_category_gewuerze:
                return R.drawable.category_background_gewuerze;
            case R.id.homepage_category_gebaeck:
                return R.drawable.category_background_gebaeck;
            case R.id.homepage_category_milchprodukte:
                return R.drawable.category_background_milchprodukte;
            case R.id.homepage_category_sueßigkeiten:
                return R.drawable.category_background_suessigkeiten;
            default:
                return R.drawable.list_background;
        }
    }

    public void refreshProductsByCategory(String category_id) {
        String url = "http://bfi.bbs-me.org:2536/api/getProducts.php";
        final String[] jsonStatus = new String[1];
        final String[] jsonMessage = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            if (jsonObject.has("status")) {
                                jsonStatus[0] = jsonObject.getString("status");
                            }
                            if (jsonObject.has("message")) {
                                jsonMessage[0] = jsonObject.getString("message");
                            }
                        } catch (JSONException e) {
                            ToastManager.showToast(HomepageActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if(jsonObject.has("status")) {
                            if(jsonStatus[0].equals("200")) {
                                ToastManager.showToast(HomepageActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                                System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                            }
                        }   else    {
                            ToastManager.showToast(HomepageActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(HomepageActivity.this, "Verbindung zwischen Api und App unterbrochen (refreshProducts)!", Toast.LENGTH_LONG);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("category_id", category_id);
                return params;
            }
        };

        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu
        MySingleton.getInstance(HomepageActivity.this).addToRequestQueue(stringRequest);
    }
}