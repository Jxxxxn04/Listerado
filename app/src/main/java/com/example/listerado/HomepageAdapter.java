package com.example.listerado;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class HomepageAdapter extends ArrayAdapter<ListItemHomepage> {

    Context context;
    SharedpreferencesManager sharedpreferencesManager;

    public HomepageAdapter(Context context, ArrayList<ListItemHomepage> items) {
        super(context, 0, items);
        this.context = context;
        sharedpreferencesManager = new SharedpreferencesManager(context);
    }





    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_homepage_item, parent, false);
        }

        ListItemHomepage item = getItem(position);



        TextView textView = convertView.findViewById(R.id.homepage_textView);
        textView.setText(item.getProduct_name());

        Button add_item_button = convertView.findViewById(R.id.add_item_button);
        add_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ToastManager.showToast(context, HomepageActivity.getSelectedLists().toString(), 0);

                ArrayList<String> selectedLists = new ArrayList<>();
                selectedLists = HomepageActivity.getSelectedLists();

                if (selectedLists.size() > 0) {
                    for (int i = 0; i < selectedLists.size(); i++) {
                        addItemToList(item.getProduct_id(), selectedLists.get(i));
                    }
                }   else {
                    ToastManager.showToast(context, "Keine Liste ausgewählt!", 0);
                }


            }
        });
        return convertView;
    }

    public void addItemToList(String product_id, String listId) {
        String url = "http://bfi.bbs-me.org:2536/api/addListProduct.php";
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
                            ToastManager.showToast(context, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                                //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");

                            }   else {
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
                        ToastManager.showToast(context, "Verbindung zwischen Api und App unterbrochen (getUserLists)!", Toast.LENGTH_LONG);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("list_id", listId);
                params.put("product_id", product_id);
                params.put("user_id", sharedpreferencesManager.getId());
                params.put("hashed_password", sharedpreferencesManager.getHashed_password());
                return params;
            }
        };

        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
