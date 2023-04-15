package com.example.controleestoquees;

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URI;


public class Api {



    /*public void testarApi(){
        System.out.println("Iniciando chamada para a Api");
        //CronetEngine.Builder myBuilder = new CronetEngine.Builder(context);
        //CronetEngine cronetEngine = myBuilder.build();

        //final TextView textView = (TextView) findViewById(R.id.text);
        // ...

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                        System.out.println("olá");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
                System.out.println("Deu erro");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }*/
}
