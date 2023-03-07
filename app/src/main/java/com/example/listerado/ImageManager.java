package com.example.listerado;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    ImageView bigProfileImageView, navbarProfileImageView;
    Context context;
    SharedPreferences sharedPreferences;
    Boolean refreshBothImageViews, successOnRefresh;
    String jsonImage;



    public ImageManager(Context context, ImageView bigProfileImageView, ImageView navbarProfileImageView) {
        this.bigProfileImageView = bigProfileImageView;
        this.navbarProfileImageView = navbarProfileImageView;
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        refreshBothImageViews = true;
    }

    public ImageManager(Context context, ImageView navbarProfileImageView) {
        this.navbarProfileImageView = navbarProfileImageView;
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        refreshBothImageViews = false;
    }


    public void refreshImage() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        //System.out.println("\n\n\n\n\n\nTEST\n\n\n\n\n\n");

        String savedID = sharedPreferences.getString("id", "");

        // Erstellen Sie die Volley-Abfrage
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "http://bfi.bbs-me.org:2536/api/getUserImage.php";
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
                            if (jsonObject.has("image")) {
                                jsonImage = jsonObject.getString("image");
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
                                editor.putString("hasImage", "1");
                                editor.apply();
                                successOnRefresh = true;
                                byte[] decodedString = Base64.decode(jsonImage, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                MyGlobals.bitmapToString = jsonImage;
                                MyGlobals.hasProfilePicture = true;
                                if (refreshBothImageViews) {
                                    bigProfileImageView.setImageBitmap(decodedByte);
                                    navbarProfileImageView.setImageBitmap(decodedByte);
                                }   else {
                                    navbarProfileImageView.setImageBitmap(decodedByte);
                                }
                                //System.out.println("\n\n\n\n\n\nErfolgreich\n\n\n\n\n\n");

                                //saves the image in a sharedpreferences
                                editor.putString("imageToString", jsonImage);
                                editor.apply();
                            }

                            if (jsonStatus[0].equals("201")) {
                                editor.putString("hasImage", "0");
                                editor.apply();
                            }
                        } else {
                            successOnRefresh = false;
                            ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                            //System.out.println("\n\n\n\n\n\nNicht Erfolgreich\n\n\n\n\n\n");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(context, "Failed!", Toast.LENGTH_LONG);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", savedID);
                return params;
            }
        };

        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void refreshImageViewFromSharedPreferences() {

        String hasImage = sharedPreferences.getString("hasImage", "Nichts Gespeichert");
        String sharedResponse = sharedPreferences.getString("imageToString", "Nichts Gespeichert");
        System.out.println("\n\n\n\n\n\nsharedResponse: " + sharedResponse + "\nhasImage: " + hasImage + "\n\n\n\n\n\n");

        if (hasImage.equals("1")) {
            byte[] decodedString = Base64.decode(sharedResponse, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            navbarProfileImageView.setImageBitmap(decodedByte);
        }

    }
}
