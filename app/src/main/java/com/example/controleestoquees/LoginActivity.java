package com.example.controleestoquees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.google.gson.Gson;


public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Olá mundo!");
                //testarApi();

                Api.login("erick", "111111", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            // Faça algo com a resposta aqui
                            System.out.println("Tudo certo");
                            System.out.println(response.body());
                            Gson gson = new Gson();
                            String responseBody = response.body().string();
                            TokenResponse tokenResponse = gson.fromJson(responseBody, TokenResponse.class);
                            String token = tokenResponse.getToken();
                            // Extrair o token do objeto TokenResponse
                            // E armazená-lo em uma string
                            Api.setToken(token);
                            System.out.println("Token: " + Api.getToken());
                        } else {
                            // Tratar o erro de resposta
                            System.out.println("Tudo errado");
                        }
                    }
                });
            }
        });

        //testarApi();
    }

    public void testarApi(){
        System.out.println("Iniciando chamada para a Api");
        //CronetEngine.Builder myBuilder = new CronetEngine.Builder(context);
        //CronetEngine cronetEngine = myBuilder.build();

        //final TextView textView = (TextView) findViewById(R.id.text);
        // ...

        // Instantiate the RequestQueue.
        //RequestQueue queue = Volley.newRequestQueue(this);
        //String url ="https://www.google.com";

        // Request a string response from the provided URL.
        /*StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                        System.out.println("olá " + response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
                System.out.println("Deu erro");
            }
        });
        */

        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Aqui você pode processar a resposta da API
                        System.out.println("Deu certo");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Aqui você pode tratar erros de rede ou da API
                        System.out.println("Deu erro");
                    }
                });

        queue.add(request);

        // Add the request to the RequestQueue.
        //queue.add(stringRequest);
        */

        try {
            //String response = Api.get("https://www.example.com");
            new HttpTask().execute("http://192.168.0.7:8000");
            System.out.println("Tudo certo");
            // Faça algo com a resposta do servidor
        } catch (Exception e) {
            // Trate exceções de rede
            System.out.println("Tudo errado");
        }
    }

    private class HttpTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                response = Api.get(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Use a resposta aqui
            System.out.println(result);
        }
    }

    public class TokenResponse {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}