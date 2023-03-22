package com.example.listerado;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddUserToListActivity extends AppCompatActivity {

    LinearLayout goToMyLists, goToHomepage, goToMyAccount;
    SharedpreferencesManager sharedpreferencesManager;
    ImageView navbarImageView;
    ImageManager imageManager;
    EditText inputText;
    ArrayList<ListItemAddUser> items;
    ListView listView;
    AddUserToListAdapter adapter;
    NotificationManager notificationManager;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_list);

        goToHomepage = findViewById(R.id.addUserToList_navigation_goToHomepage);
        goToMyLists = findViewById(R.id.addUserToList_navigation_goToMyList);
        goToMyAccount = findViewById(R.id.addUserToList_navigation_goToMyProfile);
        navbarImageView = findViewById(R.id.addUserToList_movebar_Konto_imageView);
        textView = findViewById(R.id.invite_textview_2);
        inputText = findViewById(R.id.addUserLists_inputText);
        listView = findViewById(R.id.addUserToList_listView);
        sharedpreferencesManager = new SharedpreferencesManager(this);
        notificationManager = new NotificationManager(AddUserToListActivity.this, textView);
        notificationManager.getUserInvites();
        imageManager = new ImageManager(this, navbarImageView);
        imageManager.refreshImageViewFromSharedPreferences();
        imageManager.refreshImage();

        //Loads the id of the list
        Bundle b = getIntent().getExtras();
        String id = b.getString("list_id");

        items = new ArrayList<>();
        createAdapter(id);





        //Navigation zur Homepage
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

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().trim();
                if (!searchText.isEmpty()) {
                    clearItems();
                    getUser(searchText, id);
                    System.out.println(searchText);

                } else {
                    items.clear();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void getUser(String inputText, String list_ID) {
        adapter.notifyDataSetChanged();
        String url = "http://bfi.bbs-me.org:2536/api/searchUsers.php";
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
                            ToastManager.showToast(AddUserToListActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                                try {

                                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                                    int length = jsonArray.length();

                                    for (int i = 0; i < length; i++) {
                                        JSONObject listObject = jsonArray.getJSONObject(i);
                                        String user_id = listObject.getString("user_id");
                                        String username = listObject.getString("username");
                                        String image = listObject.getString("image");
                                        items.add(new ListItemAddUser(user_id, username, image));
                                    }
                                    adapter.notifyDataSetChanged();
                                    //System.out.println("\n\n\n\n\n\nArray: " + items);
                                    //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }   else {
                                ToastManager.showToast(AddUserToListActivity.this, "Blub", Toast.LENGTH_SHORT);
                            }
                        } else {
                            ToastManager.showToast(AddUserToListActivity.this, "Fehler aufgetreten", Toast.LENGTH_SHORT);
                            //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(AddUserToListActivity.this, "Verbindung zwischen Api und App unterbrochen (getUserLists)!", Toast.LENGTH_LONG);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("list_id", list_ID);
                params.put("user_id", sharedpreferencesManager.getId());
                params.put("search", inputText);
                return params;
            }
        };

        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu
        MySingleton.getInstance(AddUserToListActivity.this).addToRequestQueue(stringRequest);
    }


    public void createAdapter(String listID) {
        // Create the adapter
        adapter = new AddUserToListAdapter(this, items, listID);
        listView.setAdapter(adapter);
    }

    public void clearItems() {
        items.clear();
    }

    public void onBackPressed() {
        startActivity(new Intent(this, MyListsActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
