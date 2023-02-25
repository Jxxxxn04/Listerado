package com.example.listerado;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText edUsername, edEmail, edPassword, edConfirm;
    Button btn;
    TextView tv;
    LinearLayout layout_username, layout_email, layout_password, layout_confirm;
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
        layout_username = findViewById(R.id.linearlayout_username);
        layout_email = findViewById(R.id.linearlayout_email);
        layout_password = findViewById(R.id.linearlayout_password);
        layout_confirm = findViewById(R.id.linearlayout_confirm);







        // Holen Sie die SharedPreferences-Instanz
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Holen Sie einen Editor, um Daten in SharedPreferences zu schreiben
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove("username");
        editor.remove("password");
        editor.remove("email");
        editor.apply();


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
                String url = "http://bfi.bbs-me.org:2536/api/createUser.php";


                // Variables of EditText to get the Entry-String
                String username = edUsername.getText().toString();
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                String confirm = edConfirm.getText().toString();


                if (username.length() == 0 || email.length() == 0 || password.length() == 0 || confirm.length() == 0) {
                    ToastManager.showToast(RegisterActivity.this, "Bitte, fülle alle felder aus!", Toast.LENGTH_SHORT);
                } else {
                    //TODO Validation of Email
                    if (password.compareTo(confirm) == 0) {

                        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        System.out.println("\n\n\n\n\n\n\n\n\n"+ response + "\n\n\n\n\n\n\n\n\n");

                                        if(response.equals("{\"status\" : \"user created\"}")) {
                                            startActivity(new Intent(RegisterActivity.this, homepageActivity.class));
                                            ToastManager.showToast(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT);
                                            editor.putString("username", edUsername.getText().toString());
                                            editor.putString("password", edPassword.getText().toString());
                                            editor.putString("email", edEmail.getText().toString());
                                            editor.apply();

                                        }
                                        switch (response) {
                                            case "{\"status\" : \"password too short\"}":
                                                ToastManager.showToast(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT);
                                                layout_password.setBackgroundResource(R.drawable.testtest);
                                                break;
                                            case "":
                                                ToastManager.showToast(RegisterActivity.this, "Problem bei API aufgetreten! \n Error: Keine Rückgabe von API bekommen", Toast.LENGTH_LONG);
                                                break;
                                            case "{\"status\" : \"empty requests\"}":
                                                ToastManager.showToast(RegisterActivity.this, "Ein Fehler ist aufgetreten! \n Error: Empty Requests", Toast.LENGTH_LONG);
                                                break;
                                            case "{\"status\" : \"username already exists\"}":
                                                ToastManager.showToast(RegisterActivity.this, "Der Benutzername ist bereits vergeben!", Toast.LENGTH_LONG);
                                                layout_username.setBackgroundResource(R.drawable.testtest);
                                                break;
                                            case "{\"status\" : \"email already exists\"}":
                                                ToastManager.showToast(RegisterActivity.this, "Die email ist bereits vergeben!", Toast.LENGTH_LONG);
                                                layout_email.setBackgroundResource(R.drawable.testtest);
                                                break;
                                        }

                                    }

                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        ToastManager.showToast(RegisterActivity.this, "Failed!", Toast.LENGTH_LONG);
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
                        ToastManager.showToast(RegisterActivity.this, "Passwörter stimmen nicht über ein!", Toast.LENGTH_SHORT);
                        layout_confirm.setBackgroundResource(R.drawable.testtest);

                    }
                }
            }
            });
}}
