package com.example.onekonekmobileapplicationfinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onekonekmobileapplicationfinal.utils.NetworkClient;
import com.example.onekonekmobileapplicationfinal.utils.SharedPrefUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class LoginPage extends AppCompatActivity {

    OkHttpClient client;
    String TAG = "onekonek";

    private static final String PREFS_NAME = "OnekonekPref";
    SharedPrefUtils sharedPrefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefUtils = new SharedPrefUtils(getApplicationContext());
        // Check if token exists in SharedPreferences
        if (sharedPrefUtils.isUserLoggedIn()) {
            // Redirect to HomePageActivity
            Intent intent = new Intent(LoginPage.this, HomePage.class);
            startActivity(intent);
            finish(); // Close LoginPage to prevent back navigation
        } else {
            // Set the login page layout if no token is found
            setContentView(R.layout.login);
        }



        client = new OkHttpClient();


        // check if there's token or auth in shared pref meaning user is currently login


        TextView emailField = findViewById(R.id.emailField);
        TextView passField = findViewById(R.id.passField);

        Button button = findViewById(R.id.loginbutton);
        button.setOnClickListener(v -> {

            String email = emailField.getText().toString();
            String pass = passField.getText().toString();
            login(email, pass, v);
        });

        ImageButton button2 = findViewById(R.id.backBttn_LoginPage);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, LandingPage.class);
            startActivity(intent);
        });

        TextView forgotPassword = findViewById(R.id.forgotpass);
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, ForgotPassword.class);
            startActivity(intent);
        });


        TextView applyNow = findViewById(R.id.applynow);
        applyNow.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, RegistrationForm.class);
            startActivity(intent);
        });


    }

    private void login(String email, String pass, View v) {
        try {
            boolean isValid = !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass);
            if (isValid) {
                // Create JSON body
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("username", email);
                    jsonBody.put("password", pass);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String jsonBodyString = jsonBody.toString();

                // Make POST request
                NetworkClient.post("/login", jsonBodyString, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            String responseBody = response.body().string();
                            try {
                                // Parse JSON response
                                JSONObject jsonResponse = new JSONObject(responseBody);
                                String token = jsonResponse.optString("token", null);
                                String auth = jsonResponse.optString("auth", null);

                                sharedPrefUtils.saveToSharedPreferences(token, auth);
                                runOnUiThread(() -> {
                                    Toast.makeText(getApplicationContext(),"Login successful. redirecting", Toast.LENGTH_LONG).show();

                                    // Navigate to Home Page
                                    Intent intent = new Intent(LoginPage.this, HomePage.class);
                                    startActivity(intent);
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to parse response", Toast.LENGTH_SHORT).show());
                            }
                        } else {
                            try {
                                assert response.body() != null;
                                JSONObject jsonResponse = new JSONObject(response.body().string());
                                String error = jsonResponse.getString("error");
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Login failed: " + error, Toast.LENGTH_SHORT).show());

                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to parse response", Toast.LENGTH_SHORT).show());
                            }
                        }
                    }
                });
            } else {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Email and Password is required...", Toast.LENGTH_SHORT).show());
                Log.e(TAG, "Email and Password is required...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

