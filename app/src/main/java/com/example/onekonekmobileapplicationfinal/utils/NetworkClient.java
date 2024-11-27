package com.example.onekonekmobileapplicationfinal.utils;
import okhttp3.*;

public class NetworkClient {

    private static final String BASE_URL = "http://13.211.183.92/auth";
    private static final OkHttpClient httpClient = new OkHttpClient();

    // GET request function
    public static void get(String endpoint, String jsonBody, Callback callback) {
        RequestBody body = RequestBody.create(
                jsonBody, MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .method("GET", body)
                .build();

        httpClient.newCall(request).enqueue(callback);
    }

    // POST request function
    public static void post(String endpoint, String jsonBody, Callback callback) {
        RequestBody body = RequestBody.create(
                jsonBody, MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(callback);
    }
}
