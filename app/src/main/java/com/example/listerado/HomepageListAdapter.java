package com.example.listerado;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class HomepageListAdapter extends ArrayAdapter<ListItemLists> {

    Context context;
    SharedpreferencesManager sharedpreferencesManager;

    public HomepageListAdapter(Context context, List<ListItemLists> items) {
        super(context, 0, items);
        this.context = context;
        sharedpreferencesManager = new SharedpreferencesManager(context);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the view if it doesn't exist
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_homepage_lists, parent, false);
        }

        TextView textView;
        textView = convertView.findViewById(R.id.homepage_lists_textview);
        RelativeLayout relativeLayout;
        relativeLayout = convertView.findViewById(R.id.homepage_lists_relativelayout);



        // Get the item at the current position
        ListItemLists item = getItem(position);

        textView.setText(item.getText());



        relativeLayout.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = true;
            @Override
            public void onClick(View view) {

                if (isClicked) {
                    isClicked = false;
                    relativeLayout.setBackgroundResource(R.drawable.homepage_lists_clicked_background);
                    HomepageActivity.addSelectedList(item.getId());
                } else {
                    relativeLayout.setBackgroundResource(R.drawable.homepage_lists_not_clicked_background);
                    isClicked = true;
                    HomepageActivity.removeListFromSelectedList(context, item.getId());
                }
            }
        });



        return convertView;
    }


}
