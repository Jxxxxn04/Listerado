package com.example.listerado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    TextView username, email,logoutButton;
    LinearLayout changeUsernameLayoutButton, NAV_account_goToHomepageLayout, NAV_account_goTomyListLayout;
    Intent switchToHomepageIntent, switchToLoginActivity, switchToMyListsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        changeUsernameLayoutButton = findViewById(R.id.change_username_layout_button);
        NAV_account_goToHomepageLayout = findViewById(R.id.account_navigation_goToHomepage);
        NAV_account_goTomyListLayout = findViewById(R.id.account_navigation_goToMyList);
        username = findViewById(R.id.myAccount_username);
        email = findViewById(R.id.myAccount_email);
        logoutButton = findViewById(R.id.logout_Button);
        logoutButton.bringToFront();


        switchToHomepageIntent = new Intent(this, HomepageActivity.class);
        switchToLoginActivity = new Intent(this, LoginActivity.class);
        switchToMyListsActivity = new Intent(this, MyListsActivity.class);

        // Holen Sie die SharedPreferences-Instanz
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Holen Sie einen Editor, um Daten in SharedPreferences zu schreiben
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String savedUsername = sharedPreferences.getString("username", "");
        String savedEmail = sharedPreferences.getString("email", "");


        username.setText(savedUsername);
        email.setText(savedEmail);



        //Navigation zur Homepage
        NAV_account_goToHomepageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(switchToHomepageIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        NAV_account_goTomyListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(switchToMyListsActivity);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });



        //Logout Button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Benutzername, Passwort und Email werden aus der SharedPreferences gelöscht
                editor.remove("username");
                editor.remove("password");
                editor.remove("email");

                // Änderungen werden gespeichert
                editor.apply();

                startActivity(switchToLoginActivity);
                finish();
            }
        });









    }

}