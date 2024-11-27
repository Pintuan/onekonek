package com.example.registrationform;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailTextField = (EditText) findViewById(R.id.l_emailTextField);
        EditText passwordTextField = (EditText) findViewById(R.id.l_passwordTextField);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailTextField.getText().toString().equals(GlobalVariable.email) &&
                        passwordTextField.getText().toString().equals(GlobalVariable.password)) {
                    Toast.makeText(login.this, "Login Success", Toast.LENGTH_SHORT).show();
                    openHomeActivity();
                } else
                    Toast.makeText(login.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void openHomeActivity(){
        Intent goToHome = new Intent(login.this,Home.class);
        startActivity(goToHome);

    }
}

    