package com.example.listerado;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;



public class JsonRequestTask extends AsyncTask<String, Void, JSONObject> {

    private VolleyResponseListener listener;
    private RequestQueue requestQueue;

    public JsonRequestTask(Context context, VolleyResponseListener listener) {
        this.listener = listener;
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        String url = urls[0];
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listener.onResponse(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
    }
}

