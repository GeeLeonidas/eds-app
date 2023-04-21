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

    public static void get(String route, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        String url = BASE_URL + route;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + Api.token) // Adicionar o token na autorização
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void registerProduct(String name, int countStock, int countStockAlert, int countStand, int countStandAlert, Callback callback) {
        String url = BASE_URL + "api/item";

        StringWriter stringWriter = new StringWriter();
        try {
            new JsonWriter(stringWriter).beginObject().
                    name("nome").value(name).
                    name("qtd").value(countStock).
                    name("qtd_alert_stock").value(countStockAlert).
                    name("qtd_alert_stand").value(countStandAlert).
                    name("qtd_stand").value(countStand).
                    endObject().flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String json = stringWriter.toString();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + Api.token)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void registerUser(String username, String name, String password, String role, Callback callback) {
        String url = BASE_URL + "user/create";

        StringWriter stringWriter = new StringWriter();
        try {
            new JsonWriter(stringWriter).beginObject().
                    name("username").value(username).
                    name("name").value(name).
                    name("password").value(password).
                    name("cargo").value(role).
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

    public static String getCargoFromToken(){
        String[] parts = Api.token.split("[.]");
        try{
            byte[] bytePart = parts[1].getBytes("UTF-8");
            String decodedPart = new String(java.util.Base64.getUrlDecoder().decode(bytePart), "UTF-8");
            String[] temp = decodedPart.split(",");
            String[] temp2 = null;
            for(String t : temp){
                if(t.contains("cargo")){
                    temp2 = t.split(":");
                    break;
                }
            }

            if(temp2 != null){
                String cargo = temp2[1].replaceAll("\""," ").trim();
                return cargo;
            }else{
                throw new RuntimeException("não foi possivel obter cargo");
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
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
