package com.example.listerado;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationManager {

    TextView textView1, textView2;

    Boolean changeTwoTextViews;
    SharedpreferencesManager sharedpreferencesManager;
    Context context;

    public NotificationManager(Context context, TextView textView1, TextView textView2) {
        this.textView1 = textView1;
        this.textView2 = textView2;
        this.context = context;
        changeTwoTextViews = true;
        sharedpreferencesManager = new SharedpreferencesManager(context);
    }

    public NotificationManager(Context context, TextView textView2) {
        this.textView2 = textView2;
        this.context = context;
        changeTwoTextViews = false;
        sharedpreferencesManager = new SharedpreferencesManager(context);
    }




    public void getUserInvites() {
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

                            if (jsonObject.has("message")) {
                                jsonMessage[0] = jsonObject.getString("message");
                            }


                        } catch (JSONException e) {
                            ToastManager.showToast(context, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");

                                try {

                                    ArrayList<String> arrayList = new ArrayList<>();

                                    JSONArray jsonArray = jsonObject.getJSONArray("invites");
                                    int length = jsonArray.length();

                                    for (int i = 0; i < length; i++) {
                                        JSONObject listObject = jsonArray.getJSONObject(i);
                                        String list_id = listObject.getString("list_id");
                                        arrayList.add(list_id);
                                    }


                                    if (changeTwoTextViews) {
                                        textView1.setText(String.valueOf(arrayList.size()));
                                    }


                                    if (arrayList.size() > 0) {
                                        textView2.setVisibility(View.VISIBLE);
                                        textView2.setText(String.valueOf(arrayList.size()));
                                    }   else {
                                        textView2.setVisibility(View.GONE);
                                    }

                                    //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }




                            }   else  {
                                ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                                //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
                            }
                        } else {
                            ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                            //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(context, "Verbindung zwischen Api und App unterbrochen (getUserInvites)!", Toast.LENGTH_LONG);
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
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


}
