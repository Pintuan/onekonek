package com.example.onekonekmobileapplicationfinal;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.onekonekmobileapplicationfinal.utils.NetworkClient;
import com.example.onekonekmobileapplicationfinal.utils.SharedPrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PaymentHistory extends Fragment {

    private LinearLayout paymentContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_history, container, false);

        paymentContainer = view.findViewById(R.id.paymentContainer);

        // Load and populate payment history data
        populatePaymentHistory();

        return view;

    }

    private void populatePaymentHistory() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        try {
            JSONObject jsonBody = new JSONObject();
            try {
                String accountId = new SharedPrefUtils(requireActivity().getApplicationContext()).getAccountId();
                jsonBody.put("authorizationToken", accountId);
                jsonBody.put("customerId", accountId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String jsonBodyString = jsonBody.toString();


            NetworkClient.post("/getTransactions", jsonBodyString, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Failed to fetch user bills: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @SuppressLint("SetTextI18n")
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        String responseBody = response.body().string();
                        try {
                            JSONArray jsonArray = new JSONArray(responseBody);

                            if (jsonArray.isNull(0)) {
                                requireActivity().runOnUiThread(() -> {
                                    View billingCard = inflater.inflate(R.layout.template_no_records, paymentContainer, false);
                                    paymentContainer.addView(billingCard);
                                });

                                return;
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                // Inflate the payment card layout
                                int finalI1 = i;
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String payment_id = jsonObject.optString("payment_id", null);
                                String total_paid = jsonObject.optString("total_paid", null);
                                String payment_date = jsonObject.optString("payment_date", null);
                                String account_id = jsonObject.optString("account_id", null);
                                String accountId = new SharedPrefUtils(requireActivity().getApplicationContext()).getAccountId();
                                System.out.println(account_id);
                                if (accountId.equals(account_id)) {
                                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(payment_date);

                                    // Format it into a human-friendly date
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
                                    String formattedDate = zonedDateTime.format(formatter);

                                    requireActivity().runOnUiThread(() -> {
                                        View paymentCard = inflater.inflate(R.layout.template_transaction_payment_card, paymentContainer, false);

                                        // Find and set the TextViews
                                        TextView paidAmount = paymentCard.findViewById(R.id.paidAmount);
                                        TextView paymentDate = paymentCard.findViewById(R.id.paymentDate);
                                        TextView transactionNumber = paymentCard.findViewById(R.id.transactionNumber);

                                        // Set dynamic data (replace with actual data)
                                        DecimalFormat df = new DecimalFormat("###,###.00");
                                        paidAmount.setText(df.format(Integer.parseInt(total_paid))); // Example: ₱1500, ₱1600, ...
                                        paymentDate.setText(formattedDate);
                                        transactionNumber.setText(payment_id); // Example: TXN1000, TXN1001, ...


                                        // Add the card to the container
                                        paymentContainer.addView(paymentCard);
                                    });
                                }



                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Failed to parse response", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        try {
                            assert response.body() != null;
                            JSONObject jsonResponse = new JSONObject(response.body().string());
                            String error = jsonResponse.getString("error");
                            requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Failed: " + error, Toast.LENGTH_SHORT).show());

                        } catch (Exception e) {
                            e.printStackTrace();
                            requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Failed to parse response", Toast.LENGTH_SHORT).show());
                        }
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
