package com.example.listerado;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class accountActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        LinearLayout changeUsernameLayoutButton = findViewById(R.id.change_username_layout_button);








        changeUsernameLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Erstellen Sie eine Animation für das LinearLayout
                Animation animation = new ScaleAnimation(1, 1.1f, 1, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(500);
                animation.setRepeatCount(1);
                animation.setRepeatMode(Animation.REVERSE);

                // Starten Sie die Animation für das LinearLayout
                v.startAnimation(animation);
            }
        });







    }

}