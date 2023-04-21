package com.example.controleestoquees;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class HomeAdmActivity extends AppCompatActivity {

    private Button btnNovo, btnUsuario;
    private EditText etxBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_adm);
        btnNovo = findViewById(R.id.btn_novo);
        btnUsuario = findViewById(R.id.btn_usuario);
        etxBuscar = findViewById(R.id.etx_buscar);

        Activity activity = this;

        btnUsuario.setOnClickListener(view -> {
            System.out.println("Indo para a tela de adição dos usuários!");
        });

        btnNovo.setOnClickListener(view -> {
            System.out.println("Indo para a tela de criação dos produtos!");
            Intent intent = new Intent(activity, CadastroProdutoActivity.class);
            startActivity(intent);
        });

        etxBuscar.addTextChangedListener(new SearchWatcher(() -> {
            System.out.println("Usuário parou de digitar!");
        }));
    }
}