package com.example.controleestoquees;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeFuncionarioActivity extends AppCompatActivity {

    private EditText etxBuscar;
    private ListView listProdutos;
    private ArrayAdapter<ProductItem> itemArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_funcionario);
        etxBuscar = findViewById(R.id.etx_buscar);
        listProdutos = findViewById(R.id.list_produtos);

        Activity activity = this;

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
    }
}