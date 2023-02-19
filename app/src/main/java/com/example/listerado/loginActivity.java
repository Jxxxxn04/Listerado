package com.example.listerado;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
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


    RequestQueue queue = Volley.newRequestQueue(loginActivity.this);
    String url = "http://bfi.bbs-me.org:2536/api/login.php";


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


        // Switch to Register Page
        tvSwitchtoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Registration", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(loginActivity.this, RegisterActivity.class ));
            }
        });

        // Button POST request
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();

                if(username.length() == 0 || password.length() == 0){
                    Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus.", Toast.LENGTH_SHORT).show();
                } else {
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //Toast.makeText(getApplicationContext(),"Done!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(loginActivity.this, homepageActivity.class));

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
                            params.put("password", edPassword.getText().toString());
                            return params;
                        }
                    };
                    queue.add(postRequest);
                }
            }
        });

    }
}