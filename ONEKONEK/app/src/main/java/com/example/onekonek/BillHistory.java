package com.example.onekonek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class BillHistory extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_bill_history, container, false);



        ImageButton downloadBttn = view.findViewById(R.id.downloadBttn);



        downloadBttn.setOnClickListener(v -> Toast.makeText(getActivity(), "Statement of Account has been successfully downloaded", Toast.LENGTH_SHORT).show());


        return view;
    }
}
