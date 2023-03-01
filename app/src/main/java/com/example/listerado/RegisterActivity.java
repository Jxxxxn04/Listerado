package com.example.listerado;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    String jsonStatus, jsonMessage, jsonID;
    Boolean codeChecked;
    EditText edUsername, edEmail, edPassword, edConfirm;
    Button btn;
    TextView tv;
    LinearLayout layout_username, layout_email, layout_password, layout_confirm;
    ImageView showPasswordImage;
    private int currentImage = 0;
    private long mLastClickTime = 0;
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
        layout_username = findViewById(R.id.register_linearlayout_username);
        layout_email = findViewById(R.id.register_linearlayout_email);
        layout_password = findViewById(R.id.register_linearlayout_password);
        layout_confirm = findViewById(R.id.register_linearlayout_confirm);
        showPasswordImage = findViewById(R.id.register_showPassword);
        showPasswordImage.setImageResource(R.mipmap.icon_hide_password);
        codeChecked = false;






        //edPassword.setInputType();


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

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        // Register Button submit POST request

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });


        edUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_username.setBackgroundResource(R.drawable.rectangle_for_login);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_email.setBackgroundResource(R.drawable.rectangle_for_login);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_password.setBackgroundResource(R.drawable.rectangle_for_login);
                layout_confirm.setBackgroundResource(R.drawable.rectangle_for_login);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_password.setBackgroundResource(R.drawable.rectangle_for_login);
                layout_confirm.setBackgroundResource(R.drawable.rectangle_for_login);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        showPasswordImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Passwort wird versteckt
                if (currentImage == 0) {
                    showPasswordImage.setImageResource(R.mipmap.icon_show_password);
                    currentImage = 1;
                    edPassword.setTransformationMethod(null);
                    edConfirm.setTransformationMethod(null);
                    //System.out.println("\n\n\n\n\n\n\n\n\n"+ currentImage + "\n\n\n\n\n\n\n\n\n");
                    edPassword.setSelection(edPassword.getText().length());
                    edConfirm.setSelection(edConfirm.getText().length());
                } else {
                    //Passwort wird angezeigt
                    showPasswordImage.setImageResource(R.mipmap.icon_hide_password);
                    currentImage = 0;
                    edPassword.setTransformationMethod(new PasswordTransformationMethod());
                    edConfirm.setTransformationMethod(new PasswordTransformationMethod());
                    //System.out.println("\n\n\n\n\n\n\n\n\n"+ currentImage + "\n\n\n\n\n\n\n\n\n");
                    edPassword.setSelection(edPassword.getText().length());
                    edConfirm.setSelection(edConfirm.getText().length());
                }
            }
        });


        //By pressing the back Button the screen will be changed to Login


    }


    public void createAccount() {
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        String url = "http://bfi.bbs-me.org:2536/api/createUser.php";


        // Variables of EditText to get the Entry-String
        String username = edUsername.getText().toString();
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        String confirm = edConfirm.getText().toString();


        if (username.length() == 0 || email.length() == 0 || password.length() == 0 || confirm.length() == 0) {
            ToastManager.showToast(RegisterActivity.this, "Bitte, fülle alle Felder aus!", Toast.LENGTH_SHORT);
        } else {
            //TODO Validation of Email
            if (password.compareTo(confirm) == 0) {

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

                                    if (jsonObject.has("id")) {
                                        jsonID = jsonObject.getString("id");
                                    }
                                    if (jsonObject.has("status")) {
                                        jsonStatus = jsonObject.getString("status");
                                    }

                                    jsonMessage = jsonObject.getString("message");


                                } catch (JSONException e) {
                                    ToastManager.showToast(RegisterActivity.this, "Failed to parse server response!", Toast.LENGTH_SHORT);
                                    e.printStackTrace();
                                    System.out.println("\n\n\n\n\n\n\n\n\nJsonObject: " + jsonObject + "\nJsonStatus: " + jsonStatus + "\njsonMessage: " + jsonMessage + "\n\n\n\n\n\n\n\n\n");
                                }

                                try {
                                    switch (jsonMessage) {
                                        case "Passwort zu kurz":
                                            ToastManager.showToast(RegisterActivity.this, jsonMessage, Toast.LENGTH_SHORT);
                                            layout_password.setBackgroundResource(R.drawable.error_field_for_text_input);
                                            break;
                                        case "":
                                            ToastManager.showToast(RegisterActivity.this, "Problem bei API aufgetreten! \n Error: Keine Rückgabe von API bekommen", Toast.LENGTH_LONG);
                                            break;
                                        case "Alle Felder ausfüllen":
                                            ToastManager.showToast(RegisterActivity.this, jsonMessage, Toast.LENGTH_LONG);
                                            break;
                                        case "Benutzername bereits vergeben":
                                        case "Benutzername enthält ungültige Zeichen":
                                        case "Benutzername zu lang":
                                            ToastManager.showToast(RegisterActivity.this, jsonMessage, Toast.LENGTH_LONG);
                                            layout_username.setBackgroundResource(R.drawable.error_field_for_text_input);
                                            break;
                                        case "Ungültige Email Adresse":
                                        case "Email Adresse bereits vergeben":
                                            ToastManager.showToast(RegisterActivity.this, jsonMessage, Toast.LENGTH_LONG);
                                            layout_email.setBackgroundResource(R.drawable.error_field_for_text_input);
                                            break;
                                        default:
                                            break;
                                    }
                                } catch (Exception e) {
                                    ToastManager.showToast(RegisterActivity.this, "Error:\njsonMessage is null!", Toast.LENGTH_LONG);
                                    e.printStackTrace();
                                }

                                showVerificationDialog();
                                if (codeChecked = true) {
                                    System.out.println("\n\n\n\n\n\n\n\n\n" + "blblblblblblblblblblaskaldjsjdjasidasd" + "\n\n\n\n\n\n\n\n\n");
                                    if (Objects.equals(jsonStatus, "200")) {
                                        layout_confirm.setBackgroundResource(R.drawable.success_field_for_text_input);
                                        layout_password.setBackgroundResource(R.drawable.success_field_for_text_input);
                                        layout_username.setBackgroundResource(R.drawable.success_field_for_text_input);
                                        layout_email.setBackgroundResource(R.drawable.success_field_for_text_input);


                                        //TODO Wenn man die APP schließt überspringt man den Login obwohl man sich nicht verifiziert hat
                                        //Ruft das Dialog-Fenster auf wo der Verifizierungscode generiert und abgefragt wird


                                        ToastManager.showToast(RegisterActivity.this, jsonMessage, Toast.LENGTH_SHORT);


                                        // Holen Sie die SharedPreferences-Instanz
                                        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                                        // Holen Sie einen Editor, um Daten in SharedPreferences zu schreiben
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        //TODO The ID has to be written in SharedPreferences
                                        editor.putString("username", edUsername.getText().toString());
                                        editor.putString("password", edPassword.getText().toString());
                                        editor.putString("email", edEmail.getText().toString());
                                        editor.putString("id", jsonID);
                                        editor.apply();

                                        ToastManager.showToast(RegisterActivity.this, jsonID, Toast.LENGTH_LONG);

                                    }

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
                        params.put("password_confirm", edConfirm.getText().toString());
                        return params;
                    }
                };
                queue.add(postRequest);


            } else {
                ToastManager.showToast(RegisterActivity.this, "Passwörter stimmen nicht überein!", Toast.LENGTH_SHORT);
                layout_confirm.setBackgroundResource(R.drawable.error_field_for_text_input);
                layout_password.setBackgroundResource(R.drawable.error_field_for_text_input);

            }
        }
    }


    public Boolean showVerificationDialog() {
        Integer code = generateRandomNumber(1111, 9999);
        String requestEmail = edEmail.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        String url = "http://bfi.bbs-me.org:2536/api/sendVerificationCode.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                params.put("email", requestEmail);
                params.put("code", code.toString());
                return params;
            }
        };


        //Dialog Fenster wird erstellt
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.code_verification_template, null);
        builder.setView(view);

        EditText field1, field2, field3, field4;

        field1 = view.findViewById(R.id.code_verification_number1);
        field2 = view.findViewById(R.id.code_verification_number2);
        field3 = view.findViewById(R.id.code_verification_number3);
        field4 = view.findViewById(R.id.code_verification_number4);

        field1.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(field2, InputMethodManager.SHOW_IMPLICIT);


        builder.setPositiveButton("Ändern", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton(code.toString(), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        builder.setPositiveButton("Abbrechen", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        //Dialog Fenster wird angezeigt
        AlertDialog dialog = builder.create();
        dialog.show();


        field1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!field1.getText().toString().equals("")) {
                    field2.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(field2, InputMethodManager.SHOW_IMPLICIT);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        field2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!field2.getText().toString().equals("")) {
                    field3.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(field3, InputMethodManager.SHOW_IMPLICIT);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        field3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!field3.getText().toString().equals("")) {
                    field4.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(field4, InputMethodManager.SHOW_IMPLICIT);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        field4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //TODO Code muss hier abgeglichen werden
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                ToastManager.showToast(RegisterActivity.this, "Blub", Toast.LENGTH_SHORT);

                String enteredCode = field1.getText().toString() + field2.getText().toString() + field3.getText().toString() + field4.getText().toString();
                System.out.println("\n\n\n\n\n\n\n\n\nEnteredCode:" + enteredCode + "\n\n\n\n\n\n\n\n\nCode: " + code);
                if (enteredCode.equals(code.toString())) {
                    codeChecked = true;
                    startActivity(new Intent(RegisterActivity.this, HomepageActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();


                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return true;
    }


    public Integer generateRandomNumber(Integer min, Integer max) {
        Random rand = new Random();
        int randomNum = rand.nextInt(max) + min;
        System.out.println(randomNum);

        return randomNum;
    }


    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }


}
