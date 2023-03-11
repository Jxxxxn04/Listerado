package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    String jsonUsername, jsonEmail, jsonStatus, jsonID, jsonHashedPassword;
    EditText edUsername, edPassword, popupSendText;
    Button btn;
    TextView tvSwitchtoRegister, tvPasswordForgot;
    LinearLayout layout_username, layout_password, parentLayout;
    ImageView showPasswordImage, popupSendImage, popupCancelImage;
    PopupWindow popupWindow;
    SharedpreferencesManager sharedpreferncesManager;
    private long mLastClickTime = 0;
    private int currentImage = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUsername = findViewById(R.id.editTextLoginUsername);
        edPassword = findViewById(R.id.editTextLoginPassword);
        btn = findViewById(R.id.buttonLogin);
        tvSwitchtoRegister = findViewById(R.id.textViewSwitchToRegister);
        tvPasswordForgot = findViewById(R.id.textViewPasswordReset);
        layout_password = findViewById(R.id.login_linearlayout_password);
        layout_username = findViewById(R.id.login_linearlayout_username);
        showPasswordImage = findViewById(R.id.login_showPassword);
        showPasswordImage.setImageResource(R.mipmap.icon_hide_password);
        parentLayout = findViewById(R.id.login_parent_linearlayout);
        sharedpreferncesManager = new SharedpreferencesManager(LoginActivity.this);




        // Create SharedP references-Instanz
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);



        //Switch Activity to RegisterActivity
        tvSwitchtoRegister.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();

        });



        //Check if SharedPrefernces file contains Username, Password and Email
        //If true set Activity to HomepageActivity
        if (sharedPreferences.contains("username") && sharedPreferences.contains("password") && sharedPreferences.contains("email")) {
            //TODO Abfrage ob gespeicherte Daten immernoch in der Datenbank vorhanden sind


            startActivity(new Intent(this, HomepageActivity.class));
            finish();

        //If false load LoginActivity
        } else {
                btn.setOnClickListener(view -> {


                    Volley.newRequestQueue(LoginActivity.this);
                    String url = "http://bfi.bbs-me.org:2536/api/loginUser.php";


                    // Variables of EditText to get the Entry-String
                    String username = edUsername.getText().toString();
                    String password = edPassword.getText().toString();



                    //Checks if Input Fields are empty
                    if (username.length() == 0 || password.length() == 0) {
                        ToastManager.showToast(LoginActivity.this, "Bitte, fülle alle Felder aus!", Toast.LENGTH_SHORT);
                    } else {

                        //Post request
                        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        //Breaks up the JSON response into several variables
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);

                                            if(jsonObject.has("id")) {
                                                jsonID = jsonObject.getString("id");
                                            }
                                            if (jsonObject.has("status")) {
                                                jsonStatus = jsonObject.getString("status");
                                            }
                                            if (jsonObject.has("username")) {
                                                jsonUsername = jsonObject.getString("username");
                                            }
                                            if (jsonObject.has("email")) {
                                                jsonEmail = jsonObject.getString("email");
                                            }
                                            if (jsonObject.has("hashed_password")) {
                                                jsonHashedPassword = jsonObject.getString("hashed_password");
                                            }

                                        } catch (JSONException e) {
                                            ToastManager.showToast(LoginActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                                            e.printStackTrace();
                                        }


                                        //System.out.println("\n\n\n\n\n\n\n\n"+ jsonStatus+ "\n\n\n\n\n\n\n\n\n");


                                        //Checks whether the Login was Successful or not
                                        if(Objects.equals(jsonStatus, "200")) {
                                            //Does that one cant spam the Login Button
                                            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                                                return;
                                            }
                                            mLastClickTime = SystemClock.elapsedRealtime();

                                            ToastManager.showToast(LoginActivity.this, "Erfolgreich eingeloggt!", Toast.LENGTH_SHORT);
                                            layout_password.setBackgroundResource(R.drawable.success_field_for_text_input);
                                            layout_username.setBackgroundResource(R.drawable.success_field_for_text_input);

                                            startActivity(new Intent(LoginActivity.this, HomepageActivity.class));
                                            finish();

                                            //Write Username, Email, Password and ID in SharedPreferences File
                                            sharedpreferncesManager.changeUsername(jsonUsername);
                                            sharedpreferncesManager.changeEmail(jsonEmail);
                                            sharedpreferncesManager.changePassword(edPassword.getText().toString());
                                            sharedpreferncesManager.changeID(jsonID);
                                            sharedpreferncesManager.changeHashedPassword(jsonHashedPassword);
                                            //System.out.println("\n\n\n\n\n\nhashedPassword: " + jsonHashedPassword + "\n\n\n\n\n\n");
                                        } else {
                                            //Set the Background of all inputs to red
                                            ToastManager.showToast(LoginActivity.this, "Benutzername oder Passwort stimmen nicht!", Toast.LENGTH_SHORT);
                                            layout_password.setBackgroundResource(R.drawable.error_field_for_text_input);
                                            layout_username.setBackgroundResource(R.drawable.error_field_for_text_input);
                                        }
                                    }

                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        ToastManager.showToast(LoginActivity.this, "Verbindung zwischen Api und App unterbrochen (loginUser)", Toast.LENGTH_LONG);
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("name", edUsername.getText().toString());
                                params.put("password", edPassword.getText().toString());
                                return params;
                            }
                        };
                        //queue.add(postRequest);
                        MySingleton.getInstance(LoginActivity.this).addToRequestQueue(postRequest);
                    }
                });
            }


        //Changes the Background of all Inputs to normal if text is changed
        edUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_password.setBackgroundResource(R.drawable.rectangle_for_login);
                layout_username.setBackgroundResource(R.drawable.rectangle_for_login);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Changes the Backround of all Inputs to normal if text is changed
        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_password.setBackgroundResource(R.drawable.rectangle_for_login);
                layout_username.setBackgroundResource(R.drawable.rectangle_for_login);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //Changes whether the Password can be seen or not
        showPasswordImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Passwort wird versteckt
                if(currentImage == 0) {
                    showPasswordImage.setImageResource(R.mipmap.icon_show_password);
                    currentImage = 1;
                    edPassword.setTransformationMethod(null);
                }   else    {
                    //Passwort wird angezeigt
                    showPasswordImage.setImageResource(R.mipmap.icon_hide_password);
                    currentImage = 0;
                    edPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });


        //Popup Window (sucks)

        /*tvPasswordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater) LoginActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ViewGroup parent = findViewById(android.R.id.content);
                View customView = layoutInflater.inflate(R.layout.forgot_password_popup, parent, false);

                popupCancelImage = customView.findViewById(R.id.popup_cancel);
                popupSendImage = customView.findViewById(R.id.popup_send);
                popupSendText = customView.findViewById(R.id.popup_ed_email);



                popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.showAtLocation(parentLayout, Gravity.CENTER, 0,0);

                popupCancelImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                popupSendImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastManager.showToast(LoginActivity.this, "Nix wurde gesendet! :3", Toast.LENGTH_SHORT);
                        //TODO popupSendText ist die Email bei welcher das Passwort zurückgesetzt werden soll, muss noch implementiert werden
                    }
                });


            }
        });*/



        }
}

