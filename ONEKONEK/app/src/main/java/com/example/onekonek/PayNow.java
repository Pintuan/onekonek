package com.example.onekonek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
public class PayNow extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paynow_page);

        ImageButton button3 = findViewById(R.id.imageButton3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayNow.this, HomePage.class);
                startActivity(intent);
            }
        });
    }

    public void OpenPayment(View view) {
        startActivity(new Intent(this, BillsPayments.class));
    }
}
