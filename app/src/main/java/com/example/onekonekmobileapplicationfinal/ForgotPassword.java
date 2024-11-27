package com.example.onekonekmobileapplicationfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onekonekmobileapplicationfinal.utils.NetworkClient;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ForgotPassword extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

        ImageButton backButton = findViewById(R.id.backbttnForgotPassword);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPassword.this, LoginPage.class);
            startActivity(intent);
            finish();
        });

        TextView emailField = findViewById(R.id.emailForgotPassword);

        Button resetButton = findViewById(R.id.resetPasswordBttn);
        resetButton.setOnClickListener(v -> showResetPasswordDialog(emailField.getText().toString()));
    }

    private void showResetPasswordDialog(String email) {
        if (!TextUtils.isEmpty(email)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Password Reset")
                    .setMessage("You're going directly to your email to reset the password.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        forgotPassword(email);
                        dialog.dismiss();
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Email is required", Toast.LENGTH_SHORT).show());
        }


    }

    private void forgotPassword(String email) {
        try {
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("email", email);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String jsonBodyString = jsonBody.toString();

            NetworkClient.post("/forgot-password", jsonBodyString, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Forgot password failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        String responseBody = response.body().string();
                        try {
                            // Parse JSON response
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            String resp = jsonResponse.optString("resp", null);

                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();

                                // Navigate to Login Page
                                Intent intent = new Intent(ForgotPassword.this, LoginPage.class);
                                startActivity(intent);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to parse response", Toast.LENGTH_SHORT).show());
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
