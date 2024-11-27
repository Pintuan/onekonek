package com.example.onekonek;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

        Button resetButton = findViewById(R.id.resetPasswordBttn);
        resetButton.setOnClickListener(v -> showResetPasswordDialog());
    }

    private void showResetPasswordDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password Reset")
                .setMessage("You're going directly to your email to reset the password.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();

                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
