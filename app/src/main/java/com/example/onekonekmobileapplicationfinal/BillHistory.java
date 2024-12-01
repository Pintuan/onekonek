package com.example.onekonekmobileapplicationfinal;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BillHistory extends Fragment {


    private LinearLayout billingContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_bill_history, container, false);
        billingContainer = view.findViewById(R.id.billingContainer);

        populateBillingHistory();

        return view;
    }


    private void populateBillingHistory() {
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


            NetworkClient.post("/getCustomerTransaction", jsonBodyString, new Callback() {
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
                                    View billingCard = inflater.inflate(R.layout.template_no_records, billingContainer, false);
                                    billingContainer.addView(billingCard);
                                });

                                return;
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                String bill_id = jsonResponse.optString("bill_id", null);
                                String due_date = jsonResponse.optString("due_date", null);
                                String stat = jsonResponse.optString("stat", null);
                                ZonedDateTime zonedDateTime = ZonedDateTime.parse(due_date);
                                double amountPaid = jsonResponse.optDouble("ammount_paid", 0);
                                double planPrice = jsonResponse.optDouble("plan_price", 0);

                                // Format it into a human-friendly date
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
                                String formattedDate = zonedDateTime.format(formatter);

                                boolean isPaid;
                                isPaid = !stat.equals("76522");

                                requireActivity().runOnUiThread(() -> {
                                    View billingCard = inflater.inflate(R.layout.template_transaction_card, billingContainer, false);

                                    TextView transactionTitle = billingCard.findViewById(R.id.soaTitle);
                                    TextView transactionDate = billingCard.findViewById(R.id.dueDate);
                                    TextView paymentStatusBadge = billingCard.findViewById(R.id.paymentStatusBadge);


                                    transactionTitle.setText("Billing Statement: " + bill_id);
                                    transactionDate.setText(formattedDate);


                                    if (isPaid) {
                                        paymentStatusBadge.setText("Paid");
                                        paymentStatusBadge.setBackgroundResource(R.drawable.badge_paid); // Green background
                                    } else {
                                        paymentStatusBadge.setText("Not Paid");
                                        paymentStatusBadge.setBackgroundResource(R.drawable.badge_not_paid); // Red background
                                    }

                                    billingContainer.addView(billingCard);
                                });

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