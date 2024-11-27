package com.example.onekonek;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

public class Plans extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plans);


        ImageButton backButton = findViewById(R.id.backBttn_Plans);
        backButton.setOnClickListener(v -> {

            finish();
        });
    }
}
