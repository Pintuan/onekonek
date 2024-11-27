package com.example.registrationform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        TextView firstnameTxtView = (TextView) findViewById(R.id.i_firstnameTextView);
        TextView lastnameTxtView = (TextView) findViewById(R.id.i_lastnameTextView);
        TextView emailTxtView = (TextView) findViewById(R.id.i_emailaddTextView);
        TextView birthdateTxtView = (TextView) findViewById(R.id.i_birthdateTextView);
        TextView contactTxtView = (TextView) findViewById(R.id.i_contactTextView);

        firstnameTxtView.setText(GlobalVariable.first_name);
        lastnameTxtView.setText(GlobalVariable.last_name);
        emailTxtView.setText(GlobalVariable.email);
        birthdateTxtView.setText(GlobalVariable.birthdate);
        contactTxtView.setText(GlobalVariable.contact);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToRegistration();
            }
        });

    }

    public void openLoginActivity(){
        Intent goToLogin = new Intent(Information.this,login.class);
        startActivity(goToLogin);
    }

    public void backToRegistration(){
        Intent backToRegistration = new Intent(Information.this,MainActivity.class);
        startActivity(backToRegistration);
    }
}
