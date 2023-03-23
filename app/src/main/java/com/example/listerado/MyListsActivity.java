package com.example.listerado;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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


public class MyListsActivity extends AppCompatActivity {

    protected static ArrayList<ListItemLists> items;
    LinearLayout NAV_myList_goToHomepageLayout, NAV_myList_goTomyAccountLayout;
    Intent switchToAccountIntent, switchToHomepageIntent;
    ImageView navbar_ProfileImageView, addListImageView;
    ListView listView;
    SharedpreferencesManager sharedpreferencesManager;
    TextView textView;
    NotificationManager notificationManager;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lists);

        NAV_myList_goToHomepageLayout = findViewById(R.id.myList_navigation_goToHomepage);
        NAV_myList_goTomyAccountLayout = findViewById(R.id.myList_navigation_goToMyProfile);
        navbar_ProfileImageView = findViewById(R.id.myLists_movebar_Konto_imageView);
        addListImageView = findViewById(R.id.myLists_add_list_button);
        listView = findViewById(R.id.myLists_listView);
        textView = findViewById(R.id.invite_textview_2);
        ImageManager imageManager = new ImageManager(MyListsActivity.this, navbar_ProfileImageView);
        imageManager.refreshImageViewFromSharedPreferences();
        imageManager.refreshImage();
        sharedpreferencesManager = new SharedpreferencesManager(MyListsActivity.this);
        notificationManager = new NotificationManager(this, textView);
        notificationManager.getUserInvites();
        getUserLists();



        switchToAccountIntent = new Intent(this, AccountActivity.class);
        switchToHomepageIntent = new Intent(this, HomepageActivity.class);

        // Datenquelle für die Liste
        items = new ArrayList<>();

        //Checks if Smartphone has Internet
        if (!isInternetConnected()) {
            Intent intent = new Intent(this, NoInternetActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }

        addListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyListsActivity.this);
                View blub = LayoutInflater.from(MyListsActivity.this).inflate(R.layout.template_simple_dialog_popup_window, null);
                builder.setView(blub);

                EditText editText = blub.findViewById(R.id.simplePopup_editTextField);

                builder.setPositiveButton("Erstellen", (dialogInterface, i) -> {
                    createList(editText.getText().toString());
                });

                builder.setNegativeButton("Abbrechen", (dialogInterface, i) -> {
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
    }


    public String createList(String listName) {
        String url = "http://bfi.bbs-me.org:2536/api/createList.php";
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

                            // TODO : owner_id eintragen in Items

                        } catch (JSONException e) {
                            ToastManager.showToast(MyListsActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {

                                //Liste wird in Array gespeichert damit sie angezeigt werden kann
                                items.add(new ListItemLists(listName, jsonList_id[0], sharedpreferencesManager.getUsername(), sharedpreferencesManager.getId()));
                                ToastManager.showToast(MyListsActivity.this, jsonMessage[0], Toast.LENGTH_LONG);
                                reloadListView();
                                //System.out.println("\n\n\n\n\n\nmessage: " + jsonMessage[0] + "\njsonListUsername: " + jsonListUsername[0] + "\nid: " + jsonList_id[0] + "\nJsonObject: " + jsonObject + "\n\n\n\n\n");
                            }   else {
                                ToastManager.showToast(MyListsActivity.this, jsonMessage[0], Toast.LENGTH_LONG);
                                //System.out.println("\n\n\n\n\n\nmessage: " + jsonMessage[0] + "\n\n\n\n\n\n");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(MyListsActivity.this, "Verbindung zwischen Api und App unterbrochen (getUserLists)!", Toast.LENGTH_LONG);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("listname", listName);
                params.put("user_id", sharedpreferencesManager.getId());
                params.put("hashed_password", sharedpreferencesManager.getHashed_password());
                return params;
            }
        };

        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu
        MySingleton.getInstance(MyListsActivity.this).addToRequestQueue(stringRequest);


        return jsonList_id[0];
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
                            ToastManager.showToast(MyListsActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                System.out.println(jsonObject);
                                try {

                                    JSONArray jsonArray = jsonObject.getJSONArray("lists");
                                    int length = jsonArray.length();

                                    for (int i = 0; i < length; i++) {
                                        JSONObject listObject = jsonArray.getJSONObject(i);
                                        String list_id = listObject.getString("list_id");
                                        String listname = listObject.getString("listname");
                                        String ownername = listObject.getString("username");
                                        String owner_id = listObject.getString("user_id");
                                        items.add(new ListItemLists(listname, list_id, ownername, owner_id));


                                    }
                                    reloadListView();
                                    //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                ToastManager.showToast(MyListsActivity.this, "Etwas ist schiefgelaufen!", Toast.LENGTH_SHORT);
                                //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(MyListsActivity.this, "Verbindung zwischen Api und App unterbrochen (getUserLists)!", Toast.LENGTH_LONG);
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
        MySingleton.getInstance(MyListsActivity.this).addToRequestQueue(stringRequest);
    }



    //Refreshes the ListView with all Lists
    public void reloadListView() {

        // Create the adapter
        MyListAdapter adapter = new MyListAdapter(this, items);
        listView.setAdapter(adapter);
    }


    public void onBackPressed() {
        startActivity(new Intent(MyListsActivity.this, HomepageActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


    public static void deleteFromList(int position) {
        //System.out.println("\n\n\n\n\n\nVorher: " + items + "\n\n\n\n\n\nposition: " + position);
        items.remove(position);
        //System.out.println("\n\n\n\n\n\nNachher: " + items + "\n\n\n\n\n\n");
    }

    public static void reloadLists(int position, String newName, String listID, String ownerName, String ownerID) {
        ListItemLists item = new ListItemLists(newName, listID, ownerName, ownerID);
        items.remove(position);
        items.add(position, item);
    }


    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}

