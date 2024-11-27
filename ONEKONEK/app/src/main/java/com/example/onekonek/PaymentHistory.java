package com.example.onekonek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class PaymentHistory extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_history, container, false);


        TextView downloadReceipt = view.findViewById(R.id.downloadOfficialReceipt);


        downloadReceipt.setOnClickListener(v -> {

            Toast.makeText(getActivity(), "Receipt has been downloaded.", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
