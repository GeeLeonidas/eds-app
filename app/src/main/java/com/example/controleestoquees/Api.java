package com.example.controleestoquees;

import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import java.io.StringWriter;
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

    private static String BASE_URL = "http://10.0.2.2:8000/";

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

    public static void register(String username, String name, String password, String cargo, Callback callback) {
        String url = BASE_URL + "user/create";

        StringWriter stringWriter = new StringWriter();
        try {
            new JsonWriter(stringWriter).beginObject().
                    name("username").value(username).
                    name("name").value(name).
                    name("password").value(password).
                    name("cargo").value(cargo).
                    endObject().flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String json = stringWriter.toString();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void login(String username, String password, Callback callback){
        String url = BASE_URL + "user/login";

        StringWriter stringWriter = new StringWriter();
        try {
            new JsonWriter(stringWriter).beginObject().
                    name("username").value(username).
                    name("password").value(password).
                    endObject().flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String json = stringWriter.toString();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);

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

    public static String getBaseUrl() {
        return BASE_URL;
    }
}
