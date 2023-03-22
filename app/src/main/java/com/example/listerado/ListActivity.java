package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ListActivity extends AppCompatActivity {

    SharedpreferencesManager sharedpreferencesManager;
    static ArrayList<ListItemProduct> items;
    TextView listName, textView;
    LinearLayout goToHomepage;
    LinearLayout toToMyLists;
    LinearLayout goToMyAccount;
    @SuppressLint("StaticFieldLeak")
    static LinearLayout parentLayout;
    ImageManager imageManager;
    CircleImageView navbarImageView;
    ListView listView;
    ListAdapter adapter;
    SwipeRefreshLayout pullToRefresh;
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        sharedpreferencesManager = new SharedpreferencesManager(ListActivity.this);
        listName = findViewById(R.id.list_listName);
        parentLayout = findViewById(R.id.list_parent_layout);
        goToHomepage = findViewById(R.id.list_navigation_goToHomepage);
        goToMyAccount = findViewById(R.id.list_navigation_goToMyProfile);
        toToMyLists = findViewById(R.id.list_navigation_goToMyList);
        navbarImageView = findViewById(R.id.list_movebar_Konto_imageView);
        pullToRefresh = findViewById(R.id.list_refreshLayout);
        listView = findViewById(R.id.list_listView);
        textView = findViewById(R.id.invite_textview_2);
        notificationManager = new NotificationManager(this, textView);
        imageManager = new ImageManager(this, navbarImageView);
        imageManager.refreshImageViewFromSharedPreferences();
        imageManager.refreshImage();
        items = new ArrayList<>();


        //Loads the id of the list
        Bundle b = getIntent().getExtras();
        String id = b.getString("id");

        //Creates the adapter for the ListView
        createAdapter(id);

        //Gets the products of the list
        getProductsFromList(id);






        goToHomepage.setOnClickListener(view -> {
            startActivity(new Intent(this, HomepageActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        toToMyLists.setOnClickListener(view -> {
            startActivity(new Intent(this, MyListsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        goToMyAccount.setOnClickListener(view -> {
            startActivity(new Intent(this, AccountActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearItems();
                if(items != null) {
                    getProductsFromList(id);
                }
                pullToRefresh.setRefreshing(false);
            }
        });

    }



    public void getProductsFromList(String listId) {
        String url = "http://bfi.bbs-me.org:2536/api/getUserListProducts.php";
        final String[] jsonStatus = new String[1];
        final String[] jsonMessage = new String[1];
        final String[] jsonListName = new String[1];

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
                            if (jsonObject.has("listname")) {
                                jsonListName[0] = jsonObject.getString("listname");
                            }

                        } catch (JSONException e) {
                            ToastManager.showToast(ListActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                ToastManager.showToast(ListActivity.this, "Produkte erfolgreich geladen", Toast.LENGTH_SHORT);
                                System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                                listName.setText(jsonListName[0]);
                                try {

                                    JSONArray jsonArray = jsonObject.getJSONArray("products");
                                    int length = jsonArray.length();

                                    for (int i = 0; i < length; i++) {
                                        JSONObject listObject = jsonArray.getJSONObject(i);
                                        //String list_id = listObject.getString("list_id");
                                        String product_id = listObject.getString("product_id");
                                        String username = listObject.getString("username");
                                        String added_date = listObject.getString("added_date");
                                        String product_name = listObject.getString("product_name");
                                        String category_name = listObject.getString("category_name");
                                        items.add(new ListItemProduct("", product_id, username, added_date, product_name, category_name));
                                    }
                                    adapter.notifyDataSetChanged();
                                    //System.out.println("\n\n\n\n\n\nArray: " + items);
                                    //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } else {
                            ToastManager.showToast(ListActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                            //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(ListActivity.this, "Verbindung zwischen Api und App unterbrochen (getUserListProducts)!", Toast.LENGTH_LONG);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("list_id", listId);
                params.put("user_id", sharedpreferencesManager.getId());
                params.put("hashed_password", sharedpreferencesManager.getHashed_password());
                return params;
            }
        };

        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu
        MySingleton.getInstance(ListActivity.this).addToRequestQueue(stringRequest);
    }

    public void createAdapter(String listID) {
        // Create the adapter
        adapter = new ListAdapter(this, items, listID);
        listView.setAdapter(adapter);
    }

    public void clearItems() {
        items.clear();
    }

    public static void deleteFromList(int position) {
        //System.out.println("\n\n\n\n\n\nVorher: " + items + "\n\n\n\n\n\nposition: " + position);
        items.remove(position);
        //System.out.println("\n\n\n\n\n\nNachher: " + items + "\n\n\n\n\n\n");
    }

    public void onBackPressed() {
        startActivity(new Intent(this, MyListsActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


    public static void setParentAlpha(float lightLevel) {
        parentLayout.setAlpha(lightLevel);
    }

}
