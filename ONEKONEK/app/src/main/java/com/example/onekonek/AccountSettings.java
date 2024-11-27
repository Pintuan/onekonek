package com.example.onekonek;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.google.android.material.textfield.TextInputEditText;


import androidx.appcompat.app.AppCompatActivity;

public class AccountSettings extends AppCompatActivity {

    private TextInputEditText inputEmail, inputMobileNum, inputBday;
    private boolean isEditable = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountsettings);

        inputEmail = findViewById(R.id.inputemail);
        inputMobileNum = findViewById(R.id.inputmobilenum);
        inputBday = findViewById(R.id.inputbday);

        ImageView imageView = findViewById(R.id.editPersonalInfo);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditable) {

                    inputEmail.setEnabled(false);
                    inputMobileNum.setEnabled(false);
                    inputBday.setEnabled(false);
                } else {

                    inputEmail.setEnabled(true);
                    inputMobileNum.setEnabled(true);
                    inputBday.setEnabled(true);
                }

                isEditable = !isEditable;
            }
        });


        ImageButton backButton = findViewById(R.id.backBttn_AccountSettings);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccountSettings.this, HomePage.class);
            startActivity(intent);
        });

        ImageView editPasswordButton = findViewById(R.id.editPass);
        editPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccountSettings.this, ChangePassword.class);
            startActivity(intent);
        });

        ImageView editBillAddButton = findViewById(R.id.editBillingAddress);
        editBillAddButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccountSettings.this, ChangeBillingAddress.class);
            startActivity(intent);
        });

        //button going to other page of changepersoninfo
        /* ImageView editBillPersonInfo = findViewById(R.id.editPersonalInfo);
        editBillPersonInfo.setOnClickListener(v -> {
            Intent intent = new Intent(AccountSettings.this, ChangePersonInfo.class);
            startActivity(intent);
        }); */
    }
}
