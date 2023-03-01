package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    Integer onClickedColorChange;
    private AlertDialog deleteUserDialog;
    private EditText deletePasswordEditText;
    TextView username, email, logoutButton;
    LinearLayout changeUsernameLayoutButton, parentLayout, NAV_account_goToHomepageLayout, NAV_account_goTomyListLayout,
                deleteUserLayoutButton, changeUserPasswordLayoutButton, changeUserEmailLayoutButton;
    Intent switchToHomepageIntent, switchToLoginActivity, switchToMyListsActivity;
    ImageView profileImageViewButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private ActivityResultLauncher<String> pickImageLauncher;
    private static final int ActivityResultCallback = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        changeUsernameLayoutButton = findViewById(R.id.change_username_layout_button);
        deleteUserLayoutButton = findViewById(R.id.account_linearlayout_delete_user);
        changeUserPasswordLayoutButton = findViewById(R.id.change_password_layout_button);
        profileImageViewButton = findViewById(R.id.account_imageView_Button);
        changeUserEmailLayoutButton = findViewById(R.id.change_email_layout_button);
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

        changeUserPasswordLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePasswordDialog();
            }
        });

        changeUserEmailLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeEmailDialog();
            }
        });

        profileImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
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
                editor.remove("id");

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

        changeUsernameLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeUsernameDialog();
            }
        });


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
                    Volley.newRequestQueue(AccountActivity.this);
                    String url = "http://bfi.bbs-me.org:2536/api/deleteUser.php";
                    // Holen Sie die SharedPreferences-Instanz
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    String savedID = sharedPreferences.getString("id", "");
                    String savedPassword = sharedPreferences.getString("password", "");

                    final String[] jsonMessage = new String[1];
                    final String[] jsonStatus = new String[1];


                    //Checks if Input Fields are empty
                    if (password.isEmpty()) {
                        ToastManager.showToast(AccountActivity.this, "Bitte, fülle alle Felder aus!", Toast.LENGTH_SHORT);
                    } else {
                        if (!password.equals(savedPassword)) {
                            ToastManager.showToast(AccountActivity.this, "Falsches Passwort!", Toast.LENGTH_SHORT);
                        } else {
                            //Post request
                            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            JSONObject jsonObject = null;
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
                                                editor.remove("username");
                                                editor.remove("password");
                                                editor.remove("email");
                                                editor.remove("id");

                                                // Änderungen werden gespeichert
                                                editor.apply();

                                                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                finish();

                                            } else {
                                                //Account löschen hat nicht funktioniert
                                                ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                                            }


                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            ToastManager.showToast(AccountActivity.this, "Failed", Toast.LENGTH_LONG);
                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("id", savedID);
                                    params.put("password", edPassword.getText().toString());
                                    System.out.println("\nId: " + savedID + "\nPassword: " + edPassword.getText().toString());
                                    return params;
                                }
                            };
                            //queue.add(postRequest);
                            MySingleton.getInstance(AccountActivity.this).addToRequestQueue(postRequest);
                        }


                    }

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
        positiveButton.setTextColor(getResources().getColor(R.color.red_cancel_delete_user_button));
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(R.color.test1));
    }


    private void showChangeUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.user_changeusername_template, null);
        builder.setView(view);

        final EditText edNewUsername = view.findViewById(R.id.changeUsername_edNewUsername);
        final EditText edPassword = view.findViewById(R.id.changeUsername_edPassword);

        builder.setPositiveButton("Ändern", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newUsername = edNewUsername.getText().toString();
                String password = edPassword.getText().toString().trim();
                if (password.isEmpty() || newUsername.isEmpty()) {
                    Toast.makeText(AccountActivity.this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show();
                } else {
                    Volley.newRequestQueue(AccountActivity.this);
                    String url = "http://bfi.bbs-me.org:2536/api/changeUserName.php";

                    // Holen Sie die SharedPreferences-Instanz
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    String savedID = sharedPreferences.getString("id", "");
                    String savedPassword = sharedPreferences.getString("password", "");

                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\nid: " + savedID + "\n\n\n\n\n\n\n\n\n");

                    final String[] jsonMessage = new String[1];
                    final String[] jsonStatus = new String[1];


                    //Checks if Input Fields are empty
                    if (!password.equals(savedPassword)) {
                        ToastManager.showToast(AccountActivity.this, "Falsches Passwort!", Toast.LENGTH_SHORT);
                    } else {
                        //Post request
                        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        JSONObject jsonObject = null;
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
                                                editor.putString("username", edNewUsername.getText().toString());
                                                editor.apply();
                                                username.setText(edNewUsername.getText().toString());


                                                ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);

                                            }
                                        } else {
                                            //Fehler aufgetreten
                                            ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_SHORT);
                                        }


                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        ToastManager.showToast(AccountActivity.this, "Failed", Toast.LENGTH_LONG);
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id", savedID);
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
        positiveButton.setTextColor(getResources().getColor(R.color.red_cancel_delete_user_button));
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(R.color.test1));


    }


    public void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.user_changepassword_template, null);
        builder.setView(view);

        final EditText edOldPassword = view.findViewById(R.id.changePassword_edOldPassword);
        final EditText edNewPassword = view.findViewById(R.id.changePassword_edNewPassword);
        final EditText edConfirm = view.findViewById(R.id.changePassword_edConfirm);


        builder.setPositiveButton("Ändern", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Volley.newRequestQueue(AccountActivity.this);
                String url = "http://bfi.bbs-me.org:2536/api/changeUserPassword.php";

                // Holen Sie die SharedPreferences-Instanz
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                final String[] jsonStatus = new String[1];
                final String[] jsonMessage = new String[1];

                String savedID = sharedPreferences.getString("id", "");
                String savedPassword = sharedPreferences.getString("password", "");

                if (!edOldPassword.getText().toString().equals(savedPassword)) {
                    ToastManager.showToast(AccountActivity.this, "Falsches Passwort", Toast.LENGTH_SHORT);
                } else {
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    try {
                                        if(jsonObject.has("status")) {
                                            jsonStatus[0] = jsonObject.getString("status");
                                        }
                                        if(jsonObject.has("message")) {
                                            jsonMessage[0] = jsonObject.getString("message");
                                        }




                                    } catch (JSONException e) {
                                        ToastManager.showToast(AccountActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                                        e.printStackTrace();

                                    }

                                    if(jsonStatus[0].equals("200")) {
                                        ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_LONG);

                                        editor.putString("password", edNewPassword.getText().toString());
                                        editor.apply();



                                    }   else    {
                                        ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_LONG);
                                    }



                                }

                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    ToastManager.showToast(AccountActivity.this, "Failed!", Toast.LENGTH_LONG);
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", savedID);
                            params.put("password", edOldPassword.getText().toString());
                            params.put("new_password", edNewPassword.getText().toString());
                            params.put("new_password_confirm", edConfirm.getText().toString());
                            return params;
                        }
                    };
                    MySingleton.getInstance(AccountActivity.this).addToRequestQueue(postRequest);
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
    }


    @SuppressLint("SuspiciousIndentation")
    public void showChangeEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.user_changeemail_template, null);
        builder.setView(view);


        EditText edNewEmail, edPassword;

        edNewEmail = view.findViewById(R.id.changeEmail_edNewEmail);
        edPassword = view.findViewById(R.id.changeEmail_edPassword);





                builder.setPositiveButton("Ändern", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Volley.newRequestQueue(AccountActivity.this);
                String url = "http://bfi.bbs-me.org:2536/api/changeUserEmail.php";


                // Holen Sie die SharedPreferences-Instanz
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                final String[] jsonStatus = new String[1];
                final String[] jsonMessage = new String[1];

                String savedID = sharedPreferences.getString("id", "");
                String savedPassword = sharedPreferences.getString("password", "");

                if (!edPassword.getText().toString().equals(savedPassword)) {
                    System.out.println(savedPassword);
                    System.out.println("\n" + edPassword.getText().toString());
                    ToastManager.showToast(AccountActivity.this, "Falsches Passwort", Toast.LENGTH_SHORT);
                } else {
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    try {
                                        if(jsonObject.has("status")) {
                                            jsonStatus[0] = jsonObject.getString("status");
                                        }
                                        if(jsonObject.has("message")) {
                                            jsonMessage[0] = jsonObject.getString("message");
                                        }




                                    } catch (JSONException e) {
                                        ToastManager.showToast(AccountActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                                        e.printStackTrace();

                                    }

                                    if(jsonObject.has("status")) {
                                        if(jsonStatus[0].equals("200")) {
                                            ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_LONG);

                                            editor.putString("email", edNewEmail.getText().toString());
                                            email.setText(edNewEmail.getText().toString());
                                            editor.apply();



                                        }
                                    }
                                       else    {
                                        ToastManager.showToast(AccountActivity.this, jsonMessage[0], Toast.LENGTH_LONG);
                                    }



                                }

                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    ToastManager.showToast(AccountActivity.this, "Failed!", Toast.LENGTH_LONG);
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", savedID);
                            params.put("password", edPassword.getText().toString());
                            params.put("new_email", edNewEmail.getText().toString());
                            return params;
                        }
                    };
                    MySingleton.getInstance(AccountActivity.this).addToRequestQueue(postRequest);
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
    }






    void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    mImageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                        // Hier können Sie das Bitmap an Ihre Volley-Anfrage senden
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).launch(intent);
        }
    }

    private void sendImageToServer(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        String url = "http://bfi.bbs-me.org:2536/api/changeUserImage.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        ToastManager.showToast(AccountActivity.this, response, Toast.LENGTH_SHORT);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error from server
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", encodedImage);
                return params;
            }
        };

        // Fügen Sie die Anfrage zu Ihrer Warteschlange hinzu
        Volley.newRequestQueue(this).add(stringRequest);
    }




    public void onBackPressed() {
        startActivity(new Intent(AccountActivity.this, HomepageActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

}