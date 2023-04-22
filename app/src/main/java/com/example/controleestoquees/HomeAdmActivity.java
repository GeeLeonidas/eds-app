package com.example.controleestoquees;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeAdmActivity extends AppCompatActivity {

    private Button btnNovo, btnUsuario;
    private EditText etxBuscar;
    private ListView listProdutos;
    private ArrayAdapter<ProductItem> itemArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_adm);
        btnNovo = findViewById(R.id.btn_novo);
        btnUsuario = findViewById(R.id.btn_usuario);
        etxBuscar = findViewById(R.id.etx_buscar);
        listProdutos = findViewById(R.id.list_produtos);

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

        Api.updateItemArray();

        itemArrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, Api.getItemArray());
        listProdutos.setAdapter(itemArrayAdapter);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        itemArrayAdapter.notifyDataSetChanged();

        Activity activity = this;
        Handler handler = new Handler();
        Runnable update = new Runnable() {
            @Override
            public void run() {
                if (!activity.isDestroyed()) {
                    Api.updateItemArray();
                    itemArrayAdapter.notifyDataSetChanged();
                }

                if (!activity.isDestroyed())
                    handler.postDelayed(this, 30_000);
            }
        };
        handler.postDelayed(update, 30_000);
    }
}