package com.example.controleestoquees;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

public class InfoProdutoFuncionarioActivity extends AppCompatActivity {

    private TextView tvNomeInfo;
    private TextView tvQtdStockInfo;
    private TextView tvMinStockInfo;
    private TextView tvQtdPrateleiraInfo;
    private TextView tvMinPrateleiraInfo;
    private ProductItem currentItem;
    private Button btnPegar;
    private Button btnRepor;
    private Button btnVoltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_produto_funcionario);
        tvNomeInfo = findViewById(R.id.tv_nome_info);
        tvQtdStockInfo = findViewById(R.id.tv_qtdstock_info);
        tvMinStockInfo = findViewById(R.id.tv_minstock_info);
        tvQtdPrateleiraInfo = findViewById(R.id.tv_qtdprateleira_info);
        tvMinPrateleiraInfo = findViewById(R.id.tv_minprateleira_info);
        btnPegar = findViewById(R.id.btn_pegar);
        btnRepor = findViewById(R.id.btn_repor);
        btnVoltar = findViewById(R.id.btn_voltar);

        currentItem = Api.popCurrentItem();
        Activity activity = this;

        updateTextViewFields();

        btnPegar.setOnClickListener(view -> {
            System.out.println("Pegando uma unidade de \"" + currentItem.name + "\"");
            currentItem = Api.addCount(currentItem, -1, 1);
            updateTextViewFields();
        });

        btnRepor.setOnClickListener(view -> {
            System.out.println("Repondo uma unidade de \"" + currentItem.name + "\"");
            currentItem = Api.addCount(currentItem, 1, 0);
            updateTextViewFields();
        });

        btnVoltar.setOnClickListener(view -> {
            System.out.println("Voltando à tela Home...");
            Intent intent = new Intent(activity, HomeFuncionarioActivity.class);
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