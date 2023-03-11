package com.example.listerado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {

    public MyListAdapter(Context context, ArrayList<String> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_my_lists, parent, false);
        }

        String item = getItem(position);

        TextView textView = convertView.findViewById(R.id.listName);
        textView.setText(item);

        ImageView deleteButton = convertView.findViewById(R.id.delete_list);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(item);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}