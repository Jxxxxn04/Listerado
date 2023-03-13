package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.concurrent.atomic.AtomicReference;


public class MyListsActivity extends AppCompatActivity {

    protected ArrayList<String> items, id;
    LinearLayout NAV_myList_goToHomepageLayout, NAV_myList_goTomyAccountLayout;
    Intent switchToAccountIntent, switchToHomepageIntent;
    ImageView navbar_ProfileImageView, addListImageView;
    ListView listView;
    SharedpreferencesManager sharedpreferencesManager;
    String[][] listArray;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lists);

        NAV_myList_goToHomepageLayout = findViewById(R.id.myList_navigation_goToHomepage);
        NAV_myList_goTomyAccountLayout = findViewById(R.id.myList_navigation_goToMyProfile);
        navbar_ProfileImageView = findViewById(R.id.myLists_navbar_ProfilImageView);
        addListImageView = findViewById(R.id.myLists_add_list_button);
        listView = findViewById(R.id.myLists_listView);
        ImageManager imageManager = new ImageManager(MyListsActivity.this, navbar_ProfileImageView);
        imageManager.refreshImageViewFromSharedPreferences();
        imageManager.refreshImage();
        sharedpreferencesManager = new SharedpreferencesManager(MyListsActivity.this);

        switchToAccountIntent = new Intent(this, AccountActivity.class);
        switchToHomepageIntent = new Intent(this, HomepageActivity.class);

        // Datenquelle für die Liste
        items = new ArrayList<>();
        id = new ArrayList<>();
        getAllListsFromUser();
        reloadAdapter();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteListFromUser(parent.getPositionForView(view));

                //Toast.makeText(getApplicationContext(), "Position: " + itemPosition, Toast.LENGTH_SHORT).show();
            }
        });


        addListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyListsActivity.this);
                View blub = LayoutInflater.from(MyListsActivity.this).inflate(R.layout.template_simple_dialog_popup_window, null);
                builder.setView(blub);

                EditText editText = blub.findViewById(R.id.simplePopup_editTextField);

                builder.setPositiveButton("Senden", (dialogInterface, i) -> {
                    items.add(editText.getText().toString());
                    addListToUser(editText.getText().toString());
                    reloadAdapter();
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
    }


    public Boolean openConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyListsActivity.this);
        AtomicReference<Boolean> isConfirmed = new AtomicReference<>(false);
        builder.setPositiveButton("Bestätigen", (dialogInterface, i) -> {
                isConfirmed.set(true);
        });

        builder.setNegativeButton("Abbrechen", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();


        return isConfirmed.get();
    }

    public void onBackPressed() {
        startActivity(new Intent(MyListsActivity.this, HomepageActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void getAllListsFromUser() {
        String url = "http://bfi.bbs-me.org:2536/api/getUserLists.php";
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
                            ToastManager.showToast(MyListsActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                System.out.println("\n\n\n\n\n\nsucess: \n" + jsonObject + "\n\n\n\n\n\n\n");
                                ToastManager.showToast(MyListsActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = jsonObject.getJSONArray("lists");
                                    int length = jsonArray.length();
                                    listArray = new String[length][2];

                                    for (int i = 0; i < length; i++) {
                                        JSONObject listObject = jsonArray.getJSONObject(i);
                                        String list_id = listObject.getString("list_id");
                                        String listname = listObject.getString("listname");
                                        //listArray[i][0] = list_id;
                                        id.add(list_id);
                                        //listArray[i][1] = listname;
                                        items.add(listname);
                                        reloadAdapter();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        } else {
                            ToastManager.showToast(MyListsActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                            System.out.println("\n\n\n\n\n\nfailure: \n" + jsonObject + "\n\n\n\n\n\n\n");
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

    public void addListToUser(String listName) {
        String url = "http://bfi.bbs-me.org:2536/api/createList.php";
        final String[] jsonStatus = new String[1];
        final String[] jsonMessage = new String[1];
        final String[] jsonList_id = new String[1];
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
                        } catch (JSONException e) {
                            ToastManager.showToast(MyListsActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                ToastManager.showToast(MyListsActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                                //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                            }
                        } else {
                            ToastManager.showToast(MyListsActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                            //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
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
    }


    public void reloadAdapter() {
        MyListAdapter adapter = new MyListAdapter(this, items, id);
        listView.setAdapter(adapter);
    }

    public void deleteListFromUser(Integer positionOfItemInListView) {
        String url = "http://bfi.bbs-me.org:2536/api/deleteList.php";
        final String[] jsonStatus = new String[1];
        final String[] jsonMessage = new String[1];
        final String[] jsonList_id = new String[1];
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
                        } catch (JSONException e) {
                            ToastManager.showToast(MyListsActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                ToastManager.showToast(MyListsActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                                //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                                getAllListsFromUser();
                            }
                        } else {
                            ToastManager.showToast(MyListsActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                            System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
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
                params.put("list_id", listArray[positionOfItemInListView][0]); //TODO list id
                params.put("user_id", sharedpreferencesManager.getId());
                params.put("hashed_password", sharedpreferencesManager.getHashed_password());
                return params;
            }
        };

        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu
        MySingleton.getInstance(MyListsActivity.this).addToRequestQueue(stringRequest);
    }


}

