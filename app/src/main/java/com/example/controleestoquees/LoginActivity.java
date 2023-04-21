package com.example.controleestoquees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.google.gson.Gson;


public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText etxUsuario, etxSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btn_login);
        etxUsuario = (EditText) findViewById(R.id.etx_usuario);
        etxSenha = (EditText) findViewById(R.id.etx_senha);
        Activity activity = this;

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Olá mundo!");
                //testarApi();

                String usuario = etxUsuario.getText().toString();
                String senha = etxSenha.getText().toString();

                if(usuario.trim().equals("")){
                   Toast toast = Toast.makeText(getApplicationContext(), "Por favor, insira nome de usuário", Toast.LENGTH_LONG);
                   toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
                   toast.show();
                }else if(senha.trim().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Por favor, insira uma senha", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
                    toast.show();
                }else {
                    etxUsuario.clearFocus();
                    etxSenha.clearFocus();
                    Api.login(usuario, senha, new Callback() {
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

                                // pegando cargo do funcionario e abrindo tela home apropriada
                                String cargo = Api.getCargoFromToken();
                                System.out.println("Cargo: " + cargo); // apagar

                                Intent intent;
                                if (cargo.equals("administrador")) {
                                    intent = new Intent(activity, HomeAdmActivity.class);
                                } else {
                                    intent = new Intent(activity, HomeFuncionarioActivity.class);
                                }
                                startActivity(intent);
                            } else {
                                // Tratar o erro de resposta
                                System.out.println("Tudo errado: " + response);
                            }
                        }
                    });

                    /*Api.get("api/teste", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJjN2RjNGFjMS1kY2I1LTExZWQtODZkNC1kODVlZDNmMDg0NDciLCJjYXJnbyI6ImdlcmVudGUiLCJpYXQiOjE2ODE4NjkwMjYsImV4cCI6MTY4MTkxMjIyNn0.Sz20B5G8swNQzE8dOmQhp66PCT1vHfvfV7HXRc1TTFA", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String responseBody = response.body().string();
                                // Fazer algo com a resposta aqui
                                System.out.println("Resposta: " + responseBody);
                            } else {
                                // Tratar o erro de resposta
                            }
                        }
                    });*/
                }
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
            AsyncTask<String, Void, String> resultTask = new HttpTask().execute(Api.getBaseUrl());
            String result = resultTask.get();
            System.out.println("Tudo certo: " + result);
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
            /*try {
                response = Api.get(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
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