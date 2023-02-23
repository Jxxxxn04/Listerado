package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    String jsonUsername, jsonEmail, jsonStatus;
    EditText edUsername, edPassword;
    Button btn;
    TextView tvSwitchtoRegister, tvPasswordForgot;

    private long mLastClickTime = 0;


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

                                            JSONObject jsonObject;
                                            try {
                                                jsonObject = new JSONObject(response);
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }

                                            try {
                                                jsonStatus = jsonObject.getString("status");
                                                jsonUsername = jsonObject.getString("username");
                                                jsonEmail = jsonObject.getString("email");
                                                int id = jsonObject.getInt("id");
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }


                                            System.out.println("\n\n\n\n\n\n\n\n"+ jsonStatus+ "\n\n\n\n\n\n\n\n\n");
                                            if(Objects.equals(jsonStatus, "login successfully")) {
                                                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                                                    return;
                                                }
                                                mLastClickTime = SystemClock.elapsedRealtime();

                                                ToastManager.showToast(loginActivity.this, "Erfolgreich eingeloggt!", Toast.LENGTH_SHORT);


                                                startActivity(new Intent(loginActivity.this, homepageActivity.class));
                                                finish();


                                                //Username, Email, Passwort werden in die SharedPreferences geschrieben
                                                editor.putString("username", jsonUsername);
                                                editor.putString("email", jsonEmail);
                                                editor.putString("password", edPassword.getText().toString());
                                                editor.apply();


                                            } else {
                                                ToastManager.showToast(loginActivity.this, "Failed Login!", Toast.LENGTH_SHORT);
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
        }
}

