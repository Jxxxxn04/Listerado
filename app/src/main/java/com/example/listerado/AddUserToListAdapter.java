package com.example.listerado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class AddUserToListAdapter extends ArrayAdapter<ListItemAddUser> {

    Context context;


    public AddUserToListAdapter(@NonNull Context context, List<ListItemAddUser> items) {
        super(context, 0, items);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the view if it doesn't exist
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_add_user_to_list, parent, false);
        }

        ImageView imageView;
        Button button;
        TextView textView;

        // Get the item at the current position
        ListItemAddUser item = getItem(position);


        // Set the text of the TextView in the view

        imageView = convertView.findViewById(R.id.add_user_to_list_image);
        button = convertView.findViewById(R.id.add_user_to_list_button);
        textView = convertView.findViewById(R.id.add_user_to_list_username);

        textView.setText(item.getUsername());


        return convertView;
    }


}
