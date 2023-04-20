package com.example.controleestoquees;

import android.widget.TextView;

import java.net.URI;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Api {
    private static OkHttpClient client = new OkHttpClient();

    private static String token = "";

    private static String BASE_URL = "http://192.168.0.7:8000/";

    /*public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }*/

    public static void get(String route, String token, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        String url = BASE_URL + route;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token) // Adicionar o token na autorização
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void login(String username, String password, Callback callback){
        //String url = "http://192.168.0.7:8000" + "/user/login";
        String url = BASE_URL + "user/login";

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}");

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void setToken(String token){
        Api.token = token;
    }

    public static String getToken(){
        return Api.token;
    }
}
