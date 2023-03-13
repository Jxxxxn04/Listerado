package com.example.listerado;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListManager {
    Context context;
    SharedpreferencesManager sharedpreferencesManager;
    String[][] listArray;


    public ListManager(Context context) {
        this.context = context;
        sharedpreferencesManager = new SharedpreferencesManager(context);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }





    public String createList(String listName) {
        final Boolean[] listCreated = {false};
        String url = "http://bfi.bbs-me.org:2536/api/createList.php";
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
                                ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                                listCreated[0] = true;
                                //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                            }
                        } else {
                            ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                            //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
                            listCreated[0] = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(context, "Verbindung zwischen Api und App unterbrochen (createList)!", Toast.LENGTH_LONG);
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
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

        if (listCreated[0]) {
            return listName;
        }   else    {
            return null;
        }
    }


    public String[][] refreshLists() {
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
                            ToastManager.showToast(context, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                System.out.println("\n\n\n\n\n\nsucess: \n" + jsonObject + "\n\n\n\n\n\n\n");
                                ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = jsonObject.getJSONArray("lists");
                                    int length = jsonArray.length();
                                    listArray = new String[length][2];

                                    for (int i = 0; i < length; i++) {
                                        JSONObject listObject = jsonArray.getJSONObject(i);
                                        String list_id = listObject.getString("list_id");
                                        String listname = listObject.getString("listname");
                                        listArray[i][0] = list_id;
                                        listArray[i][1] = listname;
                                        /*items.add(listname);
                                        reloadAdapter();*/
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        } else {
                            ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                            System.out.println("\n\n\n\n\n\nfailure: \n" + jsonObject + "\n\n\n\n\n\n\n");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(context, "Verbindung zwischen Api und App unterbrochen (getUserLists)!", Toast.LENGTH_LONG);
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

        return listArray;
    }

}
