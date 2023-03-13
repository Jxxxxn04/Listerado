package com.example.listerado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

public class HomepageAdapter extends ArrayAdapter<String> {

    Context context;
    SharedpreferencesManager sharedpreferencesManager;

    public HomepageAdapter(Context context, ArrayList<String> items) {
        super(context, 0, items);
        this.context = context;
        sharedpreferencesManager = new SharedpreferencesManager(context);
    }





    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_homepage_items, parent, false);
        }

        String item = getItem(position);

        TextView textView = convertView.findViewById(R.id.homepage_textView);
        textView.setText(item);

        RelativeLayout add_item_button = convertView.findViewById(R.id.add_item_button);
        add_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastManager.showToast(context, "blub", 0);
            }
        });
        return convertView;
    }
}
