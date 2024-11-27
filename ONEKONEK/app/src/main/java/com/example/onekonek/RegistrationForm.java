package com.example.onekonek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);


        ImageButton button1 = findViewById(R.id.backBttn_ApplicationForm);
        button1.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationForm.this, landingpage.class);
            startActivity(intent);
        });


        Spinner spinner = findViewById(R.id.plan);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.plan_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void openApplicationForm(View view) {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }

    public void OpenApplicationForm(View view) {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}
