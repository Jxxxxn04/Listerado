package com.example.listerado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.test_template_list_products, parent, false);
        }

        ImageView imageView, little_info_icon;
        imageView = convertView.findViewById(R.id.template_list_imageview);
        little_info_icon = convertView.findViewById(R.id.little_info_icon);
        TextView textView, category_name;
        textView = convertView.findViewById(R.id.template_list_textView);
        category_name = convertView.findViewById(R.id.template_list_category_name);


        // Get the item at the current position
        ListItemProduct item = getItem(position);

        textView.setText(item.getProduct_name());
        category_name.setText(item.getCategory_name());

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

        little_info_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog dialog = null;
                Date date;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View popupView = LayoutInflater.from(context).inflate(R.layout.template_popup_product_info, null);
                builder.setView(popupView);

                ImageView product_info_exit;
                product_info_exit = popupView.findViewById(R.id.product_info_exit);
                TextView product_info_product_name, product_info_username, product_info_created_at;
                product_info_product_name = popupView.findViewById(R.id.product_info_product_name);
                product_info_username = popupView.findViewById(R.id.product_info_username);
                product_info_created_at = popupView.findViewById(R.id.product_info_created_at);

                //Zeit Formatieren
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
                try {
                    date = inputFormat.parse(item.getAdded_date());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss");
                String output = outputFormat.format(date);

                product_info_product_name.setText(item.getProduct_name());
                product_info_username.setText(item.getUsername());
                product_info_created_at.setText(output);


                dialog = builder.create();
                dialog.show();


                AlertDialog finalDialog = dialog;
                product_info_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finalDialog.dismiss();
                    }
                });
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
