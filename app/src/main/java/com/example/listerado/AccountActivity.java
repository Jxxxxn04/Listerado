package com.example.listerado;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    Integer onClickedColorChange;
    private AlertDialog deleteUserDialog;
    private EditText deletePasswordEditText;
    TextView username, email, logoutButton;
    LinearLayout changeUsernameLayoutButton, parentLayout, NAV_account_goToHomepageLayout, NAV_account_goTomyListLayout, deleteUserLayoutButton;
    Intent switchToHomepageIntent, switchToLoginActivity, switchToMyListsActivity;
    PopupWindow popupWindow;
    ImageView delete_user_cancel_image, delete_user_submit_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        changeUsernameLayoutButton = findViewById(R.id.change_username_layout_button);
        deleteUserLayoutButton = findViewById(R.id.account_linearlayout_delete_user);
        NAV_account_goToHomepageLayout = findViewById(R.id.account_navigation_goToHomepage);
        NAV_account_goTomyListLayout = findViewById(R.id.account_navigation_goToMyList);
        parentLayout = findViewById(R.id.account_parentLayout);
        username = findViewById(R.id.myAccount_username);
        email = findViewById(R.id.myAccount_email);
        logoutButton = findViewById(R.id.logout_Button);
        logoutButton.bringToFront();
        onClickedColorChange = Color.parseColor("#EEEEEE");


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
                logoutButton.setBackgroundColor(onClickedColorChange);

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


        deleteUserLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteUserDialog();
            }
        });













        /*
        deleteUserLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentLayout.setAlpha(0.5F);

                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.user_delete_template, null);
                builder.setView(dialogView);

                deletePasswordEditText = dialogView.findViewById(R.id.delete_user_edPassword);

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        parentLayout.setAlpha(1.0F);
                        String password = deletePasswordEditText.getText().toString();
                        // Delete user account
                        // ...
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        parentLayout.setAlpha(1.0F);
                    }
                });

                deleteUserDialog = builder.create();
                deleteUserDialog.show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (deleteUserDialog != null) {
            deleteUserDialog.dismiss();
        }
    }*/

        /*
        deleteUserLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentLayout.setAlpha(0.5F);
                LayoutInflater layoutInflater = (LayoutInflater) AccountActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ViewGroup parent = findViewById(android.R.id.content);
                View customView = layoutInflater.inflate(R.layout.user_delete_template, parent, false);

                delete_user_cancel_image = customView.findViewById(R.id.delete_user_cancel_icon);
                delete_user_submit_image = customView.findViewById(R.id.delete_user_send_icon);

                EditText editText = customView.findViewById(R.id.delete_user_edPassword);
                editText.requestFocus();

                // Tastatur öffnen
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);



                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());

                PopupWindow popupWindow = new PopupWindow(customView, width, height);
                popupWindow.showAtLocation(deleteUserLayoutButton, Gravity.CENTER, 0,0);

                delete_user_cancel_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        parentLayout.setAlpha(1.0F);

                        // Tastatur schließen
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }
                });

                delete_user_submit_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastManager.showToast(AccountActivity.this, "Nix wurde gesendet! :3", Toast.LENGTH_SHORT);
                        //TODO popupSendText ist die Email bei welcher das Passwort zurückgesetzt werden soll, muss noch implementiert werden
                    }
                });


            }
        });*/


    }



    private void showDeleteUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.user_delete_template, null);
        builder.setView(view);

        final EditText edPassword = view.findViewById(R.id.delete_user_edPassword);

        builder.setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String password = edPassword.getText().toString().trim();
                if (password.isEmpty()) {
                    Toast.makeText(AccountActivity.this, "Bitte Passwort eingeben", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: Delete user account
                    Toast.makeText(AccountActivity.this, "Benutzerkonto gelöscht!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        //Set the custom icons for the dialog buttons
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_check, 0, 0, 0);
        positiveButton.setTextColor(getResources().getColor(R.color.test1));
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_cancel, 0, 0, 0);
        negativeButton.setTextColor(getResources().getColor(R.color.test1));
    }
}