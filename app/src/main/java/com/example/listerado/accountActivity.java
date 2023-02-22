package com.example.listerado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class accountActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        LinearLayout changeUsernameLayoutButton = findViewById(R.id.change_username_layout_button);

        LinearLayout NAV_account_goToHomepageLayout = findViewById(R.id.account_navigation_goToHomepage);

        TextView logoutButton = findViewById(R.id.logout_Button);
        logoutButton.bringToFront();

        Intent switchToHomepageIntent = new Intent(this, homepageActivity.class);

        // Holen Sie die SharedPreferences-Instanz
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Holen Sie einen Editor, um Daten in SharedPreferences zu schreiben
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Intent switchToLoginActivity = new Intent(this, loginActivity.class);




        NAV_account_goToHomepageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(switchToHomepageIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });




        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Entfernen Sie den Benutzernamen und das Passwort aus SharedPreferences
                editor.remove("username");
                editor.remove("password");

                // Speichern Sie die Änderungen
                editor.apply();

                startActivity(switchToLoginActivity);
                finish();
            }
        });


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