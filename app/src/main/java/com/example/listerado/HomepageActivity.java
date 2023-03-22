package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomepageActivity extends AppCompatActivity {

    ArrayList<ListItemHomepage> products;
    ArrayList<ListItemLists> lists;
    static ArrayList<String> selectedLists;
    DrawerLayout drawerLayout;
    LinearLayout NAV_homepage_goToMyProfileLayout, NAV_homepage_goToMyLists, homepage_button;
    ImageView appIcon, navbarProfileImageView;
    Intent switchToAccountIntent, switchToMyListsIntent;
    SwipeRefreshLayout pullToRefresh;
    RelativeLayout obst, gemuese, fleisch, fisch, milchprodukte, suessigkeiten, getraenke, gewuerze, gebaeck;
    ListView listView, listsListView;
    String selectedCategory;
    HomepageAdapter adapter;
    HomepageListAdapter listAdapter;
    SharedpreferencesManager sharedpreferencesManager;
    NotificationManager notificationManager;
    TextView textView;
    EditText searchItem;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        //Initialize the needed UI elements from the xml file
        NAV_homepage_goToMyProfileLayout = findViewById(R.id.homepage_navigation_goToMyProfile);
        NAV_homepage_goToMyLists = findViewById(R.id.homepage_navigation_goToMyList);
        drawerLayout = findViewById(R.id.drawerlayout);
        listView = findViewById(R.id.homepage_listview);
        searchItem = findViewById(R.id.searchItems);
        listsListView = findViewById(R.id.homepage_lists_listview);
        appIcon = findViewById(R.id.appIcon);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        navbarProfileImageView = findViewById(R.id.homepage_movebar_Konto_imageView);
        textView = findViewById(R.id.invite_textview_2);
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
        sharedpreferencesManager = new SharedpreferencesManager(HomepageActivity.this);
        notificationManager = new NotificationManager(HomepageActivity.this, textView);
        notificationManager.getUserInvites();
        imageManager.refreshImageViewFromSharedPreferences();
        imageManager.refreshImage();
        products = new ArrayList<>();
        lists = new ArrayList<>();
        selectedLists = new ArrayList<>();
        createProductsAdapter();
        createListsAdapter();
        refreshProductsByCategory("none");
        selectedCategory = "none";
        getUserLists();

        //Checks if Smartphone has Internet
        if (!isInternetConnected()) {
            Intent intent = new Intent(this, NoInternetActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }



        //Set the Intents
        switchToAccountIntent = new Intent(this, AccountActivity.class);
        switchToMyListsIntent = new Intent(this, MyListsActivity.class);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

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
                    clearProductsArray();
                } else {
                    obst.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearProductsArray();
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
                    clearProductsArray();
                } else {
                    gemuese.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearProductsArray();
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
                    clearProductsArray();
                } else {
                    fleisch.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearProductsArray();
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
                    clearProductsArray();
                } else {
                    fisch.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearProductsArray();
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
                    clearProductsArray();
                } else {
                    getraenke.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearProductsArray();
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
                    clearProductsArray();
                } else {
                    gewuerze.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearProductsArray();
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
                    clearProductsArray();
                } else {
                    gebaeck.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearProductsArray();
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
                    clearProductsArray();
                } else {
                    milchprodukte.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearProductsArray();
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
                    clearProductsArray();
                } else {
                    suessigkeiten.setBackgroundResource(R.drawable.list_background);
                    refreshProductsByCategory("none");
                    isClicked = true;
                    clearProductsArray();
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
                clearProductsArray();
                reloadProductsByRefresh();
                pullToRefresh.setRefreshing(false);
            }
        });

        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();

                if (text.length() > 2) {
                    clearProductsArray();
                    searchItem(text);
                } else {
                    refreshProductsByCategory("none");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                clearProductsArray();
                refreshProductsByCategory("1");
                break;
            case "2":
                clearProductsArray();
                refreshProductsByCategory("2");
                break;
            case "3":
                clearProductsArray();
                refreshProductsByCategory("3");
                break;
            case "4":
                clearProductsArray();
                refreshProductsByCategory("4");
                break;
            case "5":
                clearProductsArray();
                refreshProductsByCategory("5");
                break;
            case "6":
                clearProductsArray();
                refreshProductsByCategory("6");
                break;
            case "7":
                clearProductsArray();
                refreshProductsByCategory("7");
                break;
            case "8":
                clearProductsArray();
                refreshProductsByCategory("8");
                break;
            default:
                clearProductsArray();
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
                                System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");

                                try {
                                    JSONArray jsonArray = jsonObject.getJSONArray("products");
                                    int length = jsonArray.length();

                                    for (int i = 0; i < length; i++) {
                                        JSONObject listObject = jsonArray.getJSONObject(i);
                                        String product_id = listObject.getString("product_id");
                                        String product_name = listObject.getString("product_name");
                                        String categoryID = listObject.getString("category_id");
                                        String category_name = listObject.getString("category_name");
                                        products.add(new ListItemHomepage(product_id, product_name, categoryID, category_name));
                                    }
                                    adapter.notifyDataSetChanged();
                                    //System.out.println("\n\n\n\n\n\nArray: " + items);
                                    //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
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

    public void getUserLists() {
        String url = "http://bfi.bbs-me.org:2536/api/getUserLists.php";
        final String[] jsonStatus = new String[1];

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
                        } catch (JSONException e) {
                            ToastManager.showToast(HomepageActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                try {

                                    JSONArray jsonArray = jsonObject.getJSONArray("lists");
                                    int length = jsonArray.length();

                                    for (int i = 0; i < length; i++) {
                                        JSONObject listObject = jsonArray.getJSONObject(i);
                                        String list_id = listObject.getString("list_id");
                                        String listname = listObject.getString("listname");
                                        String owner_username = listObject.getString("username");
                                        String owner_id = listObject.getString("user_id");
                                        lists.add(new ListItemLists(listname, list_id, owner_username, owner_id));


                                    }
                                    listAdapter.notifyDataSetChanged();
                                    System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                ToastManager.showToast(HomepageActivity.this, "Etwas ist schiefgelaufen!", Toast.LENGTH_SHORT);
                                //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(HomepageActivity.this, "Verbindung zwischen Api und App unterbrochen (getUserLists)!", Toast.LENGTH_LONG);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", sharedpreferencesManager.getId());
                params.put("hashed_password", sharedpreferencesManager.getHashed_password());
                return params;
            }
        };

        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu
        MySingleton.getInstance(HomepageActivity.this).addToRequestQueue(stringRequest);
    }


    public void createProductsAdapter() {
        adapter = new HomepageAdapter(this, products);
        listView.setAdapter(adapter);
    }

    public void createListsAdapter() {
        listAdapter = new HomepageListAdapter(this, lists);
        listsListView.setAdapter(listAdapter);
    }

    public void clearProductsArray() {
        products.clear();
    }

    public static void addSelectedList(String listID) {
        selectedLists.add(listID);
    }

    public static void removeListFromSelectedList(Context context, String listID) {
        // Suchen nach einem Element mit dem Wert "Apfel"
        int index = selectedLists.indexOf(listID);

        // Entfernen des Elements, wenn es gefunden wird
        if(index != -1) {
            selectedLists.remove(index);
            //ToastManager.showToast(context, listID + ", erfolgreich entwählt", 0);
            //System.out.println(selectedLists);
        }   else {
            ToastManager.showToast(context, "Angeklickte Liste wurde nicht gefunden", 0);
            //System.out.println(selectedLists);
        }
    }

    public static ArrayList<String> getSelectedLists() {
        return selectedLists;
    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void searchItem(String search) {
        String url = "http://bfi.bbs-me.org:2536/api/searchProducts.php";
        final String[] jsonStatus = new String[1];
        final String[] jsonMessage = new String[1];
        final String[] jsonList_id = new String[1];
        final String[] jsonListUsername = new String[1];
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
                            if (jsonObject.has("list_id")) {
                                jsonList_id[0] = jsonObject.getString("list_id");
                            }
                            if (jsonObject.has("username")) {
                                jsonListUsername[0] = jsonObject.getString("username");
                            }
                        } catch (JSONException e) {
                            ToastManager.showToast(HomepageActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");

                                try {
                                    JSONArray jsonArray = jsonObject.getJSONArray("products");
                                    int length = jsonArray.length();

                                    for (int i = 0; i < length; i++) {
                                        JSONObject listObject = jsonArray.getJSONObject(i);
                                        String product_id = listObject.getString("product_id");
                                        String product_name = listObject.getString("product_name");
                                        String categoryID = listObject.getString("category_id");
                                        String category_name = listObject.getString("category_name");
                                        products.add(new ListItemHomepage(product_id, product_name, categoryID, category_name));
                                    }
                                    adapter.notifyDataSetChanged();
                                    //System.out.println("\n\n\n\n\n\nArray: " + items);
                                    //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        } else {
                            ToastManager.showToast(HomepageActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                            //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(HomepageActivity.this, "Verbindung zwischen Api und App unterbrochen (getUserLists)!", Toast.LENGTH_LONG);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("category_id", "none");
                params.put("search", search);
                return params;
            }
        };

        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu
        MySingleton.getInstance(HomepageActivity.this).addToRequestQueue(stringRequest);
    }
}