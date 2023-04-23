package com.example.controleestoquees;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {
    private static OkHttpClient client = new OkHttpClient();

    private static String token = "";

    private static String BASE_URL = "http://10.0.2.2:8000/";

    private static ProductItem[] itemArray = {};
    private static Semaphore updateSemaphore = new Semaphore(1);
    private static ProductItem currentItem;
    private static final HashMap<Integer, ProductItem> itemMap = new HashMap<>();


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

    public static void registerProduct(ProductItem productItem, Callback callback) {
        String url = BASE_URL + "api/item";

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, productItem.toJson());

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

    public static void updateItemArray()  {
        if (updateSemaphore.hasQueuedThreads())
            return;
        updateSemaphore.acquireUninterruptibly();
        Api.get("api/itens", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                updateSemaphore.release();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        if (!responseBody.isEmpty()) {
                            Gson gson = new Gson();
                            itemArray = gson.fromJson(responseBody, ProductItem[].class);
                            itemMap.clear();
                            for (ProductItem item : itemArray)
                                itemMap.put(item.id, item);
                        } else {
                            System.out.println("Body vazio");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Tudo errado: " + response);
                }
                updateSemaphore.release();
            }
        });
        updateSemaphore.acquireUninterruptibly();
        updateSemaphore.release();
    }

    public static boolean checkAuth() {
        boolean[] wasSuccessful = {false};
        Semaphore semaphore = new Semaphore(1);
        semaphore.acquireUninterruptibly();
        Api.get("api/teste", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                wasSuccessful[0] = response.isSuccessful();
                semaphore.release();
            }
        });
        semaphore.acquireUninterruptibly();
        semaphore.release();
        return wasSuccessful[0];
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

    public static void removeItemFromDatabase(ProductItem item, Callback callback) {
        if (item.id == null) {
            System.out.println("ID de produto inválido!");
            return;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        String url = BASE_URL + "api/deletarItem";

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, item.toJson());

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + Api.token) // Adicionar o token na autorização
                .delete(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void putCurrentItem(ProductItem item) {
        currentItem = item;
    }

    public static ProductItem popCurrentItem() {
        assert currentItem != null;
        ProductItem result = currentItem;
        currentItem = null;
        return result;
    }

    public static boolean hasCurrentItem() {
        return currentItem != null;
    }

    public static void modifyItem(ProductItem newItem, Callback callback) {
        if (newItem.id == null || !itemMap.containsKey(newItem.id)) {
            System.out.println("ID de produto inválido!");
            return;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        String url = BASE_URL + "api/atualizarItem";

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, newItem.toJson());

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + Api.token) // Adicionar o token na autorização
                .put(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static ProductItem addCount(ProductItem item, int stock, int stand) {
        updateItemArray();
        if (itemMap.containsKey(item.id)) {
            ProductItem storedItem = itemMap.get(item.id);
            assert storedItem != null;
            int stockDiff = storedItem.countStock - item.countStock;
            int standDiff = storedItem.countStand - item.countStand;

            if (stockDiff != stock) {
                stock += stockDiff;
            } else {
                stock = 0;
            }
            if (standDiff != stand) {
                stand += standDiff;
            } else {
                stand = 0;
            }

            if (stock == 0 && stand == 0)
                return item;

            ProductItem newItem = new ProductItem(
                    storedItem.id,
                    storedItem.name,
                    storedItem.countStock + stock,
                    storedItem.countStockAlert,
                    storedItem.countStand + stand,
                    storedItem.countStandAlert
            );
            modifyItem(newItem, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (!response.isSuccessful()) {
                        System.out.println("Tudo errado: " + response);
                    }
                }
            });

            return newItem;
        }

        return item;
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

    public static ProductItem[] getItemArray() {
        return itemArray;
    }
}
