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

import androidx.annotation.NonNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

        CircleImageView imageView;
        Button button;
        TextView textView;

        // Get the item at the current position
        ListItemAddUser item = getItem(position);


        // Set the text of the TextView in the view

        imageView = convertView.findViewById(R.id.add_user_to_list_image);
        button = convertView.findViewById(R.id.add_user_to_list_button);
        textView = convertView.findViewById(R.id.add_user_to_list_username);

        if (!item.getImage().equals("false")) {
            byte[] decodedString = Base64.decode(item.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            imageView.setImageBitmap(decodedByte);
        }



        textView.setText(item.getUsername());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastManager.showToast(context, item.getUsername(), 1);
            }
        });


        return convertView;
    }


}
