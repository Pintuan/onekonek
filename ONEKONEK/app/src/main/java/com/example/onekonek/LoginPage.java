package com.example.onekonek;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        Button button = findViewById(R.id.loginbutton);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, HomePage.class);
            startActivity(intent);
        });

        ImageButton button2 = findViewById(R.id.backBttn_LoginPage);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, landingpage.class);
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
}
