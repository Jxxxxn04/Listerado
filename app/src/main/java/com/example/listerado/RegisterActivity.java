package com.example.listerado;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {

    EditText edUsername, edEmail, edPassword, edConfirm;
    Button btn;
    TextView tv;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edUsername = findViewById(R.id.editTextRegisterUsername);
        edEmail = findViewById(R.id.editTextRegisterEmail);
        edPassword = findViewById(R.id.editTextRegisterPassword);
        edConfirm = findViewById(R.id.editTextRegisterConfirm);
        btn = findViewById(R.id.buttonRegister);
        tv = findViewById(R.id.textViewSwitchToLogin);


        // Change to Login-Page
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RegisterActivity.this, loginActivity.class));
                finish();
            }
        });

        // Register Button submit POST request

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                String url = "http://10.128.25.243:2536/api/createUser.php";


                // Variables of EditText to get the Entry-String
                String username = edUsername.getText().toString();
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                String confirm = edConfirm.getText().toString();


                if (username.length() == 0 || email.length() == 0 || password.length() == 0 || confirm.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Bitte, fülle alle Felder aus", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO Validation of Email
                    if (password.compareTo(confirm) == 0) {

                        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("{\"status\" : \"user created\"}")) {
                                            startActivity(new Intent(RegisterActivity.this, loginActivity.class));
                                            //KOmentar
                                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        if(response.equals("{\"status\" : \"user already exists\"}")) {
                                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        if(response.equals("{\"status\" : \"password too short\"}")) {
                                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
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
                                params.put("username", edUsername.getText().toString());
                                params.put("email", edEmail.getText().toString());
                                params.put("password", edPassword.getText().toString());
                                return params;
                            }
                        };
                        queue.add(postRequest);



                    } else {
                        Toast.makeText(getApplicationContext(), "Passwort stimmt nicht über ein", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            });
}}
