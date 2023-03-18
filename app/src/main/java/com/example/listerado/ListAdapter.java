package com.example.listerado;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListAdapter extends ArrayAdapter<ListItemProduct> {

    Context context;
    String listId;
    SharedpreferencesManager sharedpreferencesManager;

    public ListAdapter(@NonNull Context context, List<ListItemProduct> items, String listID) {
        super(context, 0, items);
        this.context = context;
        this.listId = listID;
        sharedpreferencesManager = new SharedpreferencesManager(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the view if it doesn't exist
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_products_in_list, parent, false);
        }

        ImageView imageView;
        imageView = convertView.findViewById(R.id.template_list_imageview);
        TextView textView;
        textView = convertView.findViewById(R.id.template_list_textView);

        // Get the item at the current position
        ListItemProduct item = getItem(position);

        textView.setText(item.getProduct_name());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("Bestätigen", (dialogInterface, i) -> {
                    deleteProductFromList(item.getProduct_id(), position);
                });

                builder.setNegativeButton("Abbrechen", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return convertView;
    }


    public void deleteProductFromList(String product_id, int position) {
        String url = "http://bfi.bbs-me.org:2536/api/removeListProduct.php";
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

                        } catch (JSONException e) {
                            ToastManager.showToast(context, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                                //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                                ListActivity.deleteFromList(position);
                                notifyDataSetChanged();
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
                        ToastManager.showToast(context, "Verbindung zwischen Api und App unterbrochen (getUserListProducts)!", Toast.LENGTH_LONG);
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
