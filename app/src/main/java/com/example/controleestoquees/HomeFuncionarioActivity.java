package com.example.controleestoquees;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_funcionario);
        etxBuscar = findViewById(R.id.etx_buscar);
        listProdutos = findViewById(R.id.list_produtos);

        Activity activity = this;

        etxBuscar.addTextChangedListener(new SearchWatcher(() -> {
            System.out.println("Usuário parou de digitar!");
            handler.post(() -> itemArrayAdapter.getFilter().filter(etxBuscar.getText()));
        }));

        listProdutos.setOnItemClickListener((parent, view, position, id) -> {
            Api.putCurrentItem(Api.getItemArray()[(int) id]);
            System.out.println("Usuário clicou no item " + id);
            Intent intent = new Intent(activity, InfoProdutoAdmActivity.class);
            startActivity(intent);
        });

        new Thread(() -> {
            Api.updateItemArray();
            handler.post(() -> {
                itemArrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, Api.getItemArray());
                listProdutos.setAdapter(itemArrayAdapter);
                itemArrayAdapter.notifyDataSetChanged();
            });
        }).start();

        // Inicia o serviço de notificações
        Intent intent = new Intent(activity, NotificationService.class);
        startService(intent);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        itemArrayAdapter.notifyDataSetChanged();

        Activity activity = this;
        Runnable update = new Runnable() {
            @Override
            public void run() {
                if (!activity.isDestroyed() && !activity.isActivityTransitionRunning()) {
                    new Thread(() -> {
                        Api.updateItemArray();
                        handler.post(() -> itemArrayAdapter.notifyDataSetChanged());
                    }).start();
                }

                if (!activity.isDestroyed() && !activity.isActivityTransitionRunning())
                    handler.postDelayed(this, 30_000);
            }
        };
        handler.postDelayed(update, 30_000);
    }
}