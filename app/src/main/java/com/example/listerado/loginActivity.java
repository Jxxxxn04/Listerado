package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class loginActivity extends AppCompatActivity {

    EditText edUsername, edPassword;
    Button btn;
    TextView tvSwitchtoRegister, tvPasswordForgot;

   Boolean isClicked = false;

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





        // Holen Sie die SharedPreferences-Instanz
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Holen Sie einen Editor, um Daten in SharedPreferences zu schreiben
        SharedPreferences.Editor editor = sharedPreferences.edit();







        tvSwitchtoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Registration", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(loginActivity.this, RegisterActivity.class));
                finish();

            }
        });


        // Holen Sie die SharedPreferences-Instanz

        // Überprüfen Sie, ob ein Benutzername und Passwort gespeichert sind
        if (sharedPreferences.contains("username") && sharedPreferences.contains("password")) {







            startActivity(new Intent(this, homepageActivity.class));
            finish();
        } else {

            if(!isClicked) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        isClicked = true;

                        RequestQueue queue = Volley.newRequestQueue(loginActivity.this);
                        String url = "http://bfi.bbs-me.org:2536/api/login.php";


                        // Variables of EditText to get the Entry-String
                        String username = edUsername.getText().toString();
                        String password = edPassword.getText().toString();


                        if (username.length() == 0 || password.length() == 0) {
                            Toast.makeText(getApplicationContext(), "Bitte, fülle alle Felder aus", Toast.LENGTH_SHORT).show();
                        } else {

                            //Post request
                            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(response.equals("{\"status\" : \"Login successfully! \"}")) {
                                                Toast.makeText(getApplicationContext(), "Login correct!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(loginActivity.this, homepageActivity.class));
                                                finish();

                                                // Schreiben Sie den Benutzernamen und das Passwort in SharedPreferences
                                                editor.putString("username", "MeinBenutzername");
                                                editor.putString("password", "MeinPasswort");


                                                editor.apply();

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Failed Login!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_LONG).show();
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
}
