package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomepageActivity extends AppCompatActivity {

    ArrayList<String> items;
    LinearLayout NAV_homepage_goToMyProfileLayout, NAV_homepage_goToMyLists;
    ImageView appIcon, navbarProfileImageView;
    Intent switchToAccountIntent, switchToMyListsIntent;
    SwipeRefreshLayout pullToRefresh;
    RelativeLayout obst, gemuese, fleisch, fisch, milchprodukte, suessigkeiten, getraenke, gewuerze, gebaeck;
    ListView listView;
    String selectedCategory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        //Initialize the needed UI elements from the xml file
        NAV_homepage_goToMyProfileLayout = findViewById(R.id.homepage_navigation_goToMyProfile);
        NAV_homepage_goToMyLists = findViewById(R.id.homepage_navigation_goToMyList);
        listView = findViewById(R.id.homepage_listview);
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
        items = new ArrayList<>();
        refreshProductsByCategory("none");
        selectedCategory = "none";


        //Set the Intents
        switchToAccountIntent = new Intent(this, AccountActivity.class);
        switchToMyListsIntent = new Intent(this, MyListsActivity.class);


        obst.setOnClickListener(new View.OnClickListener() {
            final String id = "1";
            Boolean isClicked = true;

            @Override
            public void onClick(View view) {
                selectedCategory = "1";
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                    clearListArray();
                } else {
                    obst.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearListArray();
                }
            }
        });


        gemuese.setOnClickListener(new View.OnClickListener() {
            final String id = "2";
            Boolean isClicked = true;

            @Override
            public void onClick(View view) {
                selectedCategory = "2";
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                    clearListArray();
                } else {
                    gemuese.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearListArray();
                }
            }
        });

        fleisch.setOnClickListener(new View.OnClickListener() {
            final String id = "3";
            Boolean isClicked = true;

            @Override
            public void onClick(View view) {
                selectedCategory = "3";
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                    clearListArray();
                } else {
                    fleisch.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearListArray();
                }
            }
        });

        fisch.setOnClickListener(new View.OnClickListener() {
            final String id = "4";
            Boolean isClicked = true;

            @Override
            public void onClick(View view) {
                selectedCategory = "4";
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                    clearListArray();
                } else {
                    fisch.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearListArray();
                }
            }
        });

        getraenke.setOnClickListener(new View.OnClickListener() {
            final String id = "7";
            Boolean isClicked = true;

            @Override
            public void onClick(View view) {
                selectedCategory = "7";
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                    clearListArray();
                } else {
                    getraenke.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearListArray();
                }
            }
        });

        gewuerze.setOnClickListener(new View.OnClickListener() {
            final String id = "8";
            Boolean isClicked = true;

            @Override
            public void onClick(View view) {
                selectedCategory = "8";
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                    clearListArray();
                } else {
                    gewuerze.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearListArray();
                }
            }
        });

        gebaeck.setOnClickListener(new View.OnClickListener() {
            final String id = "9";
            Boolean isClicked = true;
            @Override
            public void onClick(View view) {
                selectedCategory = "9";
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                    clearListArray();
                } else {
                    gebaeck.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearListArray();
                }
            }
        });

        milchprodukte.setOnClickListener(new View.OnClickListener() {
            final String id = "5";
            Boolean isClicked = true;

            @Override
            public void onClick(View view) {
                selectedCategory = "5";
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                    clearListArray();
                } else {
                    milchprodukte.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearListArray();
                }
            }
        });

        suessigkeiten.setOnClickListener(new View.OnClickListener() {
            final String id = "6";
            Boolean isClicked = true;

            @Override
            public void onClick(View view) {
                selectedCategory = "6";
                if (isClicked) {
                    handleSelection(view);
                    refreshProductsByCategory(id);
                    isClicked = false;
                    clearListArray();
                } else {
                    suessigkeiten.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearListArray();
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
                clearListArray();
                reloadProductsByRefresh();
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

    @SuppressLint("NonConstantResourceId")
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

    public void reloadProductsByRefresh() {
        switch (selectedCategory) {
            case "1":
                clearListArray();
                refreshProductsByCategory("1");
                break;
            case "2":
                clearListArray();
                refreshProductsByCategory("2");
                break;
            case "3":
                clearListArray();
                refreshProductsByCategory("3");
                break;
            case "4":
                clearListArray();
                refreshProductsByCategory("4");
                break;
            case "5":
                clearListArray();
                refreshProductsByCategory("5");
                break;
            case "6":
                clearListArray();
                refreshProductsByCategory("6");
                break;
            case "7":
                clearListArray();
                refreshProductsByCategory("7");
                break;
            case "8":
                clearListArray();
                refreshProductsByCategory("8");
                break;
            default:
                clearListArray();
                refreshProductsByCategory("none");
                break;
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

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");

                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = jsonObject.getJSONArray("products");
                                    int length = jsonArray.length();

                                    for (int i = 0; i < length; i++) {
                                        JSONObject listObject = jsonArray.getJSONObject(i);
                                        String newProduct = listObject.getString("product_name");
                                        items.add(newProduct);
                                        refreshAdapter();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } else {
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
                params.put("amount", "5");
                return params;
            }
        };

        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu
        MySingleton.getInstance(HomepageActivity.this).addToRequestQueue(stringRequest);
    }


    public void refreshAdapter() {
        HomepageAdapter adapter = new HomepageAdapter(this, items);
        listView.setAdapter(adapter);
    }

    public void clearListArray() {
        items.clear();
    }
}