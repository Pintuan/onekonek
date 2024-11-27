package com.example.registrationform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView name = (TextView) findViewById(R.id.fullnameTextView);

        name.setText(GlobalVariable.last_name + "," + " " + GlobalVariable.first_name);

        Button backToReg = (Button) findViewById(R.id.backToRegButton);
        backToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToRegistration();
            }
        });
    }
    public void backToRegistration(){
        Intent backToRegistration = new Intent(Home.this,MainActivity.class);
        startActivity(backToRegistration);
    }
}