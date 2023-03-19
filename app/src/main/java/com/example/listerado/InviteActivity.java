package com.example.listerado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class InviteActivity extends AppCompatActivity {

    static ArrayList<ListItemInvites> items;
    LinearLayout goToHomepage, goToMyLists, goToMyAccount;
    TextView textView;
    ListView listView;
    ImageView navbarImageView;
    InviteAdapter adapter;
    SharedpreferencesManager sharedpreferencesManager;
    ImageManager imageManager;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);

        goToHomepage = findViewById(R.id.invite_navigation_goToHomepage);
        goToMyLists = findViewById(R.id.invite_navigation_goToMyList);
        goToMyAccount = findViewById(R.id.invite_navigation_goToMyProfile);
        navbarImageView = findViewById(R.id.invites_movebar_Konto_imageView);
        listView = findViewById(R.id.invites_listView);
        textView = findViewById(R.id.invite_textview_2);
        swipeRefreshLayout = findViewById(R.id.invite_refreshlayout);
        sharedpreferencesManager = new SharedpreferencesManager(InviteActivity.this);
        imageManager = new ImageManager(this, navbarImageView);
        imageManager.refreshImageViewFromSharedPreferences();
        imageManager.refreshImage();
        items = new ArrayList<>();
        getInvites();
        createAdapter();




        goToHomepage.setOnClickListener(view -> {
            startActivity(new Intent(this, HomepageActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        goToMyLists.setOnClickListener(view -> {
            startActivity(new Intent(this, MyListsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        goToMyAccount.setOnClickListener(view -> {
            startActivity(new Intent(this, AccountActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                getInvites();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    public void getInvites() {
        String url = "http://bfi.bbs-me.org:2536/api/getUserListInvites.php";
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


                        } catch (JSONException e) {
                            ToastManager.showToast(InviteActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");

                                try {
                                    JSONArray jsonArray = jsonObject.getJSONArray("invites");
                                    int length = jsonArray.length();

                                    for (int i = 0; i < length; i++) {
                                        JSONObject listObject = jsonArray.getJSONObject(i);
                                        String list_id = listObject.getString("list_id");
                                        String listname = listObject.getString("listname");
                                        String owner_id = listObject.getString("owner_id");
                                        String owner_username = listObject.getString("owner_username");
                                        items.add(new ListItemInvites(list_id, listname, owner_id, owner_username));
                                    }

                                    adapter.notifyDataSetChanged();
                                    //System.out.println("\n\n\n\n\n\n" + items + "\n\n\n\n\n\n");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }




                            }   else  {
                                ToastManager.showToast(InviteActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                                //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
                            }
                        } else {
                            ToastManager.showToast(InviteActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                            //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(InviteActivity.this, "Verbindung zwischen Api und App unterbrochen (getUserInvites)!", Toast.LENGTH_LONG);
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
        MySingleton.getInstance(InviteActivity.this).addToRequestQueue(stringRequest);
    }

    public void createAdapter() {
        // Create the adapter
        System.out.println("Liste: " + items);
        adapter = new InviteAdapter(this, items);
        listView.setAdapter(adapter);
    }



    public static void deleteItemFromItems(int position) {
        items.remove(position);
    }

    public void onBackPressed() {
        startActivity(new Intent(InviteActivity.this, AccountActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
