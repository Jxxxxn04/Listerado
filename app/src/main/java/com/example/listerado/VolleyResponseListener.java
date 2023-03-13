package com.example.listerado;

import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyResponseListener {
    void onResponse(JSONObject response) throws JSONException, InterruptedException;

    void onError(String message);
}
