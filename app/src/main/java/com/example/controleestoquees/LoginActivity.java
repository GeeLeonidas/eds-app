package com.example.controleestoquees;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;

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
                testarApi();
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
}