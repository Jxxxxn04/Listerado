package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    Integer onClickedColorChange;
    TextView username, email, logoutButton;
    LinearLayout changeUsernameLayoutButton, parentLayout, NAV_account_goToHomepageLayout, NAV_account_goTomyListLayout,
            deleteUserLayoutButton, changeUserPasswordLayoutButton, changeUserEmailLayoutButton;
    Intent switchToHomepageIntent, switchToLoginActivity, switchToMyListsActivity;
    CircleImageView profileImageViewButton, navbar_profileImageView;
    int SELECT_PICTURE = 200;
    Bitmap bitmap;
    File file;
    SharedpreferencesManager sharedpreferncesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        changeUsernameLayoutButton = findViewById(R.id.change_username_layout_button);
        deleteUserLayoutButton = findViewById(R.id.account_linearlayout_delete_user);
        changeUserPasswordLayoutButton = findViewById(R.id.change_password_layout_button);
        profileImageViewButton = findViewById(R.id.account_imageView_Button);
        navbar_profileImageView = findViewById(R.id.account_movebar_Konto_imageView);
        changeUserEmailLayoutButton = findViewById(R.id.change_email_layout_button);
        NAV_account_goToHomepageLayout = findViewById(R.id.account_navigation_goToHomepage);
        NAV_account_goTomyListLayout = findViewById(R.id.account_navigation_goToMyList);
        parentLayout = findViewById(R.id.account_parentLayout);
        username = findViewById(R.id.myAccount_username);
        email = findViewById(R.id.myAccount_email);
        logoutButton = findViewById(R.id.logout_Button);
        logoutButton.bringToFront();
        onClickedColorChange = Color.parseColor("#EEEEEE");

        ImageManager imageManager = new ImageManager(AccountActivity.this, profileImageViewButton, navbar_profileImageView);
        imageManager.refreshImageViewFromSharedPreferences();
        imageManager.refreshImage();
        sharedpreferncesManager = new SharedpreferencesManager(AccountActivity.this);
        username.setText(sharedpreferncesManager.getUsername());
        email.setText(sharedpreferncesManager.getEmail());


        //TODO abfrage ob sich username oder email geändert hat


        switchToHomepageIntent = new Intent(this, HomepageActivity.class);
        switchToLoginActivity = new Intent(this, LoginActivity.class);
        switchToMyListsActivity = new Intent(this, MyListsActivity.class);


        //Navigation zur Homepage
        NAV_account_goToHomepageLayout.setOnClickListener(view -> {
            startActivity(switchToHomepageIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        NAV_account_goTomyListLayout.setOnClickListener(view -> {
            startActivity(switchToMyListsActivity);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        profileImageViewButton.setOnClickListener(view -> showImageSelector());
        changeUserPasswordLayoutButton.setOnClickListener(view -> showChangePasswordDialog());
        changeUserEmailLayoutButton.setOnClickListener(view -> showChangeEmailDialog());

        //Logout Button
        logoutButton.setOnClickListener(view -> {
            logoutButton.setBackgroundColor(onClickedColorChange);

            // Benutzername, Passwort und Email werden aus der SharedPreferences gelöscht
            sharedpreferncesManager.clearSharedPreferences();

            startActivity(switchToLoginActivity);
            finish();
        });

        deleteUserLayoutButton.setOnClickListener(view -> showDeleteUserDialog());
        changeUsernameLayoutButton.setOnClickListener(view -> showChangeUsernameDialog());
    }

    private void showDeleteUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.template_user_delete, null);
        builder.setView(view);

        final EditText edPassword = view.findViewById(R.id.delete_user_edPassword);

        builder.setPositiveButton("Löschen", (dialogInterface, i) -> {
            String password = edPassword.getText().toString().trim();
            if (password.isEmpty()) {
                Toast.makeText(AccountActivity.this, "Bitte Passwort eingeben", Toast.LENGTH_SHORT).show();
            } else {
                Volley.newRequestQueue(AccountActivity.this);
                String url = "http://bfi.bbs-me.org:2536/api/deleteUser.php";

                String savedID = sharedpreferncesManager.getId();
                String savedPassword = sharedpreferncesManager.getPassword();
                final String[] jsonMessage = new String[1];
                final String[] jsonStatus = new String[1];

                //Checks if Input Fields are empty
                if (!password.equals(savedPassword)) {
                    ToastManager.showToast(AccountActivity.this, "Falsches Passwort!", Toast.LENGTH_SHORT);
                } else {
                    //Post request
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            response -> {

                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println("\n\n\n\n\n\n\n\n\n" + response + "\n\n\n\n\n\n\n\n\n");

                                //Breaks up the JSON response into several variables
                                try {

                                    if (jsonObject.has("status")) {
                                        jsonStatus[0] = jsonObject.getString("status");
                                    }
                                    if (jsonObject.has("message")) {
                                        jsonMessage[0] = jsonObject.getString("message");
                                    }

                                    System.out.println("\n\n\n\n\n\n\n\n" + response + "\n\n\n\n\n\n\n\n\n");
                                } catch (JSONException e) {
                                    ToastManager.showToast(AccountActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                                    e.printStackTrace();
                                    System.out.println("\n\n\n\n\n\n\n\n\nJsonObject: " + jsonObject + "\nJsonStatus: " + jsonStatus[0] + "\njsonMessage: " + jsonMessage[0] + "\n\n\n\n\n\n\n\n\n");
                                }

                                if (jsonStatus[0].equals("200")) {
                                    //Account wurde erfolgreich gelöscht
                                    ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);

                                    // Benutzername, Passwort und Email werden aus der SharedPreferences gelöscht
                                    sharedpreferncesManager.clearSharedPreferences();

                                    startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();

                                } else {
                                    //Account löschen hat nicht funktioniert
                                    ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                                }
                            },
                            error -> ToastManager.showToast(AccountActivity.this, "Verbindung zwischen Api und App unterbrochen (deleteUser)", Toast.LENGTH_LONG)
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("user_id", savedID);
                            params.put("password", edPassword.getText().toString());
                            //System.out.println("\nId: " + savedID + "\nPassword: " + edPassword.getText().toString());
                            return params;
                        }
                    };
                    //queue.add(postRequest);
                    MySingleton.getInstance(AccountActivity.this).addToRequestQueue(postRequest);
                }
            }
        });

        builder.setNegativeButton("Abbrechen", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

        //Set the custom icons for the dialog buttons
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.red_cancel_delete_user_button));
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(R.color.test1));
    }


    private void showChangeUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.template_user_changeusername, null);
        builder.setView(view);

        final EditText edNewUsername = view.findViewById(R.id.changeUsername_edNewUsername);
        final EditText edPassword = view.findViewById(R.id.changeUsername_edPassword);

        builder.setPositiveButton("Ändern", (dialogInterface, i) -> {
            String newUsername = edNewUsername.getText().toString();
            String password = edPassword.getText().toString().trim();
            if (password.isEmpty() || newUsername.isEmpty()) {
                Toast.makeText(AccountActivity.this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show();
            } else {
                Volley.newRequestQueue(AccountActivity.this);
                String url = "http://bfi.bbs-me.org:2536/api/changeUserName.php";

                String savedID = sharedpreferncesManager.getId();
                String savedPassword = sharedpreferncesManager.getPassword();

                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\nid: " + savedID + "\n\n\n\n\n\n\n\n\n");

                final String[] jsonMessage = new String[1];
                final String[] jsonStatus = new String[1];

                //Checks if Input Fields are empty
                if (!password.equals(savedPassword)) {
                    ToastManager.showToast(AccountActivity.this, "Falsches Passwort!", Toast.LENGTH_SHORT);
                } else {
                    //Post request
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            response -> {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println("\n\n\n\n\n\n\n\n\n" + response + "\n\n\n\n\n\n\n\n\n");

                                //Breaks up the JSON response into several variables
                                try {
                                    if (jsonObject.has("status")) {
                                        jsonStatus[0] = jsonObject.getString("status");
                                    }
                                    if (jsonObject.has("message")) {
                                        jsonMessage[0] = jsonObject.getString("message");
                                    }

                                    System.out.println("\n\n\n\n\n\n\n\n" + response + "\n\n\n\n\n\n\n\n\n");
                                } catch (JSONException e) {
                                    ToastManager.showToast(AccountActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                                    e.printStackTrace();
                                    System.out.println("\n\n\n\n\n\n\n\n\nJsonObject: " + jsonObject + "\nJsonStatus: " + jsonStatus[0] + "\njsonMessage: " + jsonMessage[0] + "\n\n\n\n\n\n\n\n\n");
                                }

                                if (jsonObject.has("status")) {
                                    if (jsonStatus[0].equals("200")) {
                                        //Benutzernamen erfolgreich geändert
                                        sharedpreferncesManager.changeUsername(edNewUsername.getText().toString());
                                        username.setText(edNewUsername.getText().toString());
                                        ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);

                                    }
                                } else {
                                    //Fehler aufgetreten
                                    ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                                }
                            },
                            error -> ToastManager.showToast(AccountActivity.this, "Verbindung zwischen Api und App unterbrochen (changeUsername)", Toast.LENGTH_LONG)
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("user_id", savedID);
                            params.put("password", edPassword.getText().toString());
                            params.put("new_username", edNewUsername.getText().toString());
                            System.out.println("\n\n\n\n\n\n\nid: " + savedID + "\npassword: " + edPassword.getText().toString() + "\nnew_username: " + edNewUsername.getText().toString());
                            return params;
                        }
                    };
                    //queue.add(postRequest);
                    MySingleton.getInstance(AccountActivity.this).addToRequestQueue(postRequest);
                }
            }
        });

        builder.setNegativeButton("Abbrechen", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

        //Set the custom icons for the dialog buttons
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.red_cancel_delete_user_button));
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(R.color.test1));
    }

    public void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.template_user_changepassword, null);
        builder.setView(view);

        final EditText edOldPassword = view.findViewById(R.id.changePassword_edOldPassword);
        final EditText edNewPassword = view.findViewById(R.id.changePassword_edNewPassword);
        final EditText edConfirm = view.findViewById(R.id.changePassword_edConfirm);

        builder.setPositiveButton("Ändern", (dialogInterface, i) -> {
            Volley.newRequestQueue(AccountActivity.this);
            String url = "http://bfi.bbs-me.org:2536/api/changeUserPassword.php";

            final String[] jsonStatus = new String[1];
            final String[] jsonMessage = new String[1];

            String savedID = sharedpreferncesManager.getId();
            String savedPassword = sharedpreferncesManager.getPassword();

            if (!edOldPassword.getText().toString().equals(savedPassword)) {
                ToastManager.showToast(AccountActivity.this, "Falsches Passwort", Toast.LENGTH_SHORT);
            } else {
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                if (jsonObject.has("status")) {
                                    jsonStatus[0] = jsonObject.getString("status");
                                }
                                if (jsonObject.has("message")) {
                                    jsonMessage[0] = jsonObject.getString("message");
                                }
                            } catch (JSONException e) {
                                ToastManager.showToast(AccountActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                                e.printStackTrace();
                            }

                            if (jsonStatus[0].equals("200")) {
                                ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_LONG);

                                sharedpreferncesManager.changePassword(edNewPassword.getText().toString());
                            } else {
                                ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_LONG);
                            }
                        },
                        error -> ToastManager.showToast(AccountActivity.this, "Verbindung zwischen Api und App unterbrochen (changePassword)!", Toast.LENGTH_LONG)
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", savedID);
                        params.put("password", edOldPassword.getText().toString());
                        params.put("new_password", edNewPassword.getText().toString());
                        params.put("new_password_confirm", edConfirm.getText().toString());
                        return params;
                    }
                };
                MySingleton.getInstance(AccountActivity.this).addToRequestQueue(postRequest);
            }
        });

        builder.setNegativeButton("Abbrechen", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("SuspiciousIndentation")
    public void showChangeEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.template_user_changeemail, null);
        builder.setView(view);

        EditText edNewEmail, edPassword;

        edNewEmail = view.findViewById(R.id.changeEmail_edNewEmail);
        edPassword = view.findViewById(R.id.changeEmail_edPassword);

        builder.setPositiveButton("Ändern", (dialogInterface, i) -> {
            Volley.newRequestQueue(AccountActivity.this);
            String url = "http://bfi.bbs-me.org:2536/api/changeUserEmail.php";

            final String[] jsonStatus = new String[1];
            final String[] jsonMessage = new String[1];


            String savedID = sharedpreferncesManager.getId();
            String savedPassword = sharedpreferncesManager.getPassword();

            if (!edPassword.getText().toString().equals(savedPassword)) {
                System.out.println(savedPassword);
                System.out.println("\n" + edPassword.getText().toString());
                ToastManager.showToast(AccountActivity.this, "Falsches Passwort", Toast.LENGTH_SHORT);
            } else {
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                if (jsonObject.has("status")) {
                                    jsonStatus[0] = jsonObject.getString("status");
                                }
                                if (jsonObject.has("message")) {
                                    jsonMessage[0] = jsonObject.getString("message");
                                }
                            } catch (JSONException e) {
                                ToastManager.showToast(AccountActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                                e.printStackTrace();
                            }

                            if (jsonObject.has("status")) {
                                if (jsonStatus[0].equals("200")) {
                                    ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_LONG);
                                    sharedpreferncesManager.changeEmail(edNewEmail.getText().toString());
                                    email.setText(edNewEmail.getText().toString());
                                }
                            } else {
                                ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_LONG);
                            }
                        },
                        error -> ToastManager.showToast(AccountActivity.this, "Verbindung zwischen Api und App unterbrochen (changeEmail)!", Toast.LENGTH_LONG)
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", savedID);
                        params.put("password", edPassword.getText().toString());
                        params.put("new_email", edNewEmail.getText().toString());
                        return params;
                    }
                };
                MySingleton.getInstance(AccountActivity.this).addToRequestQueue(postRequest);
            }
        });

        builder.setNegativeButton("Abbrechen", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showImageSelector() {

        ImagePicker.with(this)
                .cropSquare()                            //Crop image(Optional), Check Customization for more option
                .compress(1024)                    //Final image size will be less than 1 MB(Optional)
                .maxResultSize(512, 512)        //Final image resolution will be less than 1080 x 1080(Optional)
                .start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            // Holen Sie sich die Uri des ausgewählten Bilds
            Uri imageUri = data.getData();

            // 3. Schritt: Speichern des Bilds in einer Datei und als Bitmap
            try {
                // Konvertieren Sie die Uri in eine Bitmap
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                // Speichern Sie die Bitmap in einer Datei
                File file = new File(getCacheDir(), "image.jpeg");
                FileOutputStream fos = new FileOutputStream(file);
                fos.flush();
                fos.close();

                // 4. Schritt: Senden Sie das Bild an die API mittels Volley
                sendImageToApi(file);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Fehler beim Speichern des Bilds", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sendImageToApi(File imageFile) {
        // Konvertieren Sie das Bitmap in einen Base64-kodierten String
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String savedID = sharedpreferncesManager.getId();

        // Erstellen Sie die Volley-Abfrage
        RequestQueue requestQueue = Volley.newRequestQueue(AccountActivity.this);
        String url = "http://bfi.bbs-me.org:2536/api/changeUserImage.php";
        final String[] jsonStatus = new String[1];
        final String[] jsonMessage = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            if (jsonObject.has("status")) {
                                jsonStatus[0] = jsonObject.getString("status");
                            }
                            if (jsonObject.has("message")) {
                                jsonMessage[0] = jsonObject.getString("message");
                            }
                        } catch (JSONException e) {
                            ToastManager.showToast(AccountActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }
                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                                profileImageViewButton.setImageBitmap(bitmap);
                                navbar_profileImageView.setImageBitmap(bitmap);
                            }
                        } else {
                            ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(AccountActivity.this, "Verbindung zwischen Api und App unterbrochen (uploadImage)!", Toast.LENGTH_LONG);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", savedID);
                params.put("hashed_password", sharedpreferncesManager.getHashed_password());
                params.put("image", encodedImage);
                return params;
            }
        };
        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu
        MySingleton.getInstance(AccountActivity.this).addToRequestQueue(stringRequest);
    }

    public byte[] getFileDataFromDrawable(Context context, Uri uri) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            InputStream iStream = context.getContentResolver().openInputStream(uri);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = iStream.read(bytes)) > 0) {
                byteArrayOutputStream.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public void onBackPressed() {
        startActivity(new Intent(AccountActivity.this, HomepageActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}