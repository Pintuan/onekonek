package com.example.onekonekmobileapplicationfinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class CheckAvailability extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkavailability);


        ImageButton backButton = findViewById(R.id.backBttn_CheckAvailability);
        backButton.setOnClickListener(v -> {

            finish();
        });



    }

    public void OpenMainServer(View view) {
        Intent intent = new Intent(this, RegistrationForm.class);
        startActivity(intent);
    }

    public void OpenSanSebServer(View view) {
        Intent intent = new Intent(this, RegistrationForm.class);
        startActivity(intent);
    }

    public void OpenSanIsidroServer(View view) {
        Intent intent = new Intent(this, RegistrationForm.class);
        startActivity(intent);
    }

    public void OpenPalapatServer(View view) {
        Intent intent = new Intent(this, RegistrationForm.class);
        startActivity(intent);
    }
}
