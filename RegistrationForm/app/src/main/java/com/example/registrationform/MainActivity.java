package com.example.registrationform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText firstname = (EditText) findViewById(R.id.firstname_TextField);
        EditText lastname = (EditText) findViewById(R.id.lastname_TextField);
        EditText email = (EditText) findViewById(R.id.email_TextField);
        EditText birthdate = (EditText) findViewById(R.id.date_TextField);
        EditText contact = (EditText) findViewById(R.id.contact_TextField);
        EditText password = (EditText) findViewById(R.id.Password_TextField);

        Button view = (Button) findViewById(R.id.viewButton);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.first_name = firstname.getText().toString();
                GlobalVariable.last_name = lastname.getText().toString();
                GlobalVariable.email = email.getText().toString();
                GlobalVariable.birthdate = birthdate.getText().toString();
                GlobalVariable.contact = contact.getText().toString();
                GlobalVariable.password = password.getText().toString();


                Intent goToInformation = new Intent(MainActivity.this,login.class);
                startActivity(goToInformation);

            }
        });
    }
}