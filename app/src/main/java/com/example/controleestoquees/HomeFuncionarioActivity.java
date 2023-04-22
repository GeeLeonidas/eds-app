package com.example.controleestoquees;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class HomeFuncionarioActivity extends AppCompatActivity {

    private EditText etxBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_funcionario);
        etxBuscar = findViewById(R.id.etx_buscar);
        Activity activity = this;

        etxBuscar.addTextChangedListener(new SearchWatcher(() -> {
            System.out.println("Usuário parou de digitar!");
        }));

        Api.updateItemArray();
    }
}