package com.example.listerado;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class loginActivity extends AppCompatActivity {

    String jsonUsername, jsonEmail, jsonStatus, id;
    EditText edUsername, edPassword, popupSendText;
    Button btn;
    TextView tvSwitchtoRegister, tvPasswordForgot;
    LinearLayout layout_username, layout_password, parentLayout;
    ImageView showPasswordImage, popupSendImage, popupCancelImage;
    PopupWindow popupWindow;

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
        parentLayout = findViewById(R.id.login_parent_linearlayout);




        // Holen Sie die SharedP references-Instanz
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Holen Sie einen Editor, um Daten in SharedPreferences zu schreiben
        SharedPreferences.Editor editor = sharedPreferences.edit();



        tvSwitchtoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this, RegisterActivity.class));
                finish();

            }
        });


        // Holen Sie die SharedPreferences-Instanz

        // Überprüfen Sie, ob ein Benutzername und Passwort gespeichert sind
        if (sharedPreferences.contains("username") && sharedPreferences.contains("password") && sharedPreferences.contains("email")) {

            //TODO Abfrage ob gespeicherte Daten immernoch in der Datenbank vorhanden sind





            startActivity(new Intent(this, homepageActivity.class));
            finish();
        } else {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        RequestQueue queue = Volley.newRequestQueue(loginActivity.this);
                        String url = "http://bfi.bbs-me.org:2536/api/login.php";


                        // Variables of EditText to get the Entry-String
                        String username = edUsername.getText().toString();
                        String password = edPassword.getText().toString();




                        if (username.length() == 0 || password.length() == 0) {
                            ToastManager.showToast(loginActivity.this, "Bitte, fülle alle felder aus!", Toast.LENGTH_SHORT);
                        } else {

                            //Post request
                            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                jsonStatus = jsonObject.getString("status");
                                                jsonUsername = jsonObject.getString("username");
                                                jsonEmail = jsonObject.getString("email");
                                                id = jsonObject.getString("id");


                                            } catch (JSONException e) {
                                                ToastManager.showToast(loginActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                                                e.printStackTrace();
                                            }


                                            System.out.println("\n\n\n\n\n\n\n\n"+ jsonStatus+ "\n\n\n\n\n\n\n\n\n");
                                            if(Objects.equals(jsonStatus, "login successfully")) {
                                                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                                                    return;
                                                }
                                                mLastClickTime = SystemClock.elapsedRealtime();

                                                ToastManager.showToast(loginActivity.this, "Erfolgreich eingeloggt!", Toast.LENGTH_SHORT);

                                                layout_password.setBackgroundResource(R.drawable.success_field_for_text_input);
                                                layout_username.setBackgroundResource(R.drawable.success_field_for_text_input);


                                                startActivity(new Intent(loginActivity.this, homepageActivity.class));
                                                finish();


                                                //Username, Email, Passwort werden in die SharedPreferences geschrieben
                                                editor.putString("username", jsonUsername);
                                                editor.putString("email", jsonEmail);
                                                editor.putString("password", edPassword.getText().toString());
                                                editor.putString("id", id);
                                                editor.apply();


                                            } else {
                                                ToastManager.showToast(loginActivity.this, "Benutzername oder Passwort stimmen nicht!", Toast.LENGTH_SHORT);
                                                layout_password.setBackgroundResource(R.drawable.error_field_for_text_input);
                                                layout_username.setBackgroundResource(R.drawable.error_field_for_text_input);
                                            }
                                        }

                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            ToastManager.showToast(loginActivity.this, "Failed", Toast.LENGTH_LONG);
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
                            MySingleton.getInstance(loginActivity.this).addToRequestQueue(postRequest);
                        }
                    };
                });
            }


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

        //Change the Background of Inputs if something is wrong with the Login
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


        //Passwort wird angezeigt und versteckt
        showPasswordImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Passwort wird versteckt
                if(currentImage == 0) {
                    showPasswordImage.setImageResource(R.mipmap.icon_hide_password);
                    currentImage = 1;
                    edPassword.setTransformationMethod(null);
                }   else    {
                    //Passwort wird angezeigt
                    showPasswordImage.setImageResource(R.mipmap.icon_show_password);
                    currentImage = 0;
                    edPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        tvPasswordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater) loginActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                        ToastManager.showToast(loginActivity.this, "Nix wurde gesendet! :3", Toast.LENGTH_SHORT);
                        //TODO popupSendText ist die Email bei welcher das Passwort zurückgesetzt werden soll, muss noch implementiert werden
                    }
                });


            }
        });



        }
}

