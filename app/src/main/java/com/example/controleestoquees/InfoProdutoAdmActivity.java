package com.example.controleestoquees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InfoProdutoAdmActivity extends AppCompatActivity {

    private TextView tvNomeInfo;
    private TextView tvQtdStockInfo;
    private TextView tvMinStockInfo;
    private TextView tvQtdPrateleiraInfo;
    private TextView tvMinPrateleiraInfo;
    private ProductItem currentItem;
    private Button btnDeletar;
    private Button btnPegar;
    private Button btnAlterar;
    private Button btnRepor;
    private Button btnVoltar;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_produto_adm);
        tvNomeInfo = findViewById(R.id.tv_nome_info);
        tvQtdStockInfo = findViewById(R.id.tv_qtdstock_info);
        tvMinStockInfo = findViewById(R.id.tv_minstock_info);
        tvQtdPrateleiraInfo = findViewById(R.id.tv_qtdprateleira_info);
        tvMinPrateleiraInfo = findViewById(R.id.tv_minprateleira_info);
        btnDeletar = findViewById(R.id.btn_deletar);
        btnPegar = findViewById(R.id.btn_pegar);
        btnAlterar = findViewById(R.id.btn_alterar);
        btnRepor = findViewById(R.id.btn_repor);
        btnVoltar = findViewById(R.id.btn_voltar);

        currentItem = Api.popCurrentItem();
        handler = new Handler();
        Activity activity = this;

        updateTextViewFields();

        btnDeletar.setOnClickListener(view -> {
            System.out.println("Deletando o item \"" + currentItem.name + "\"");
            Api.updateItemArray();
            Api.removeItemFromDatabase(currentItem, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.isSuccessful()) {
                        System.out.println("Produto removido com sucesso!");
                        handler.post(() -> {
                            Toast successToast = Toast.makeText(activity, "Produto \"" + currentItem.name + "\" removido com sucesso!", Toast.LENGTH_LONG);
                            successToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
                            successToast.show();
                            Intent intent = new Intent(activity, HomeAdmActivity.class);
                            startActivity(intent);
                        });
                    } else {
                        System.out.println("Falha na remoção do produto: " + response);
                        handler.post(() -> {
                            Toast failureToast = Toast.makeText(activity, "Falha na remoção do produto", Toast.LENGTH_LONG);
                            failureToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
                            failureToast.show();
                        });
                    }
                }
            });
        });

        btnPegar.setOnClickListener(view -> {
            System.out.println("Pegando uma unidade de \"" + currentItem.name + "\"");
            currentItem = Api.addCount(currentItem, -1, 1);
            updateTextViewFields();
        });

        btnAlterar.setOnClickListener(view -> {
            System.out.println("Alterando o item \"" + currentItem.name + "\"");
        });

        btnRepor.setOnClickListener(view -> {
            System.out.println("Repondo uma unidade de \"" + currentItem.name + "\"");
            currentItem = Api.addCount(currentItem, 1, 0);
            updateTextViewFields();
        });

        btnVoltar.setOnClickListener(view -> {
            System.out.println("Voltando à tela Home...");
            Intent intent = new Intent(activity, HomeAdmActivity.class);
            startActivity(intent);
        });
    }

    private void updateTextViewFields() {
        tvNomeInfo.setText(currentItem.name);
        tvQtdStockInfo.setText(String.valueOf(currentItem.countStock));
        tvMinStockInfo.setText(String.valueOf(currentItem.countStockAlert));
        tvQtdPrateleiraInfo.setText(String.valueOf(currentItem.countStand));
        tvMinPrateleiraInfo.setText(String.valueOf(currentItem.countStandAlert));
    }
}