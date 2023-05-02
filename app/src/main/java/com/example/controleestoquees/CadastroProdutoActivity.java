package com.example.controleestoquees;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CadastroProdutoActivity extends AppCompatActivity {

    private Button btnVoltar, btnSalvarCad;
    private EditText etxNomeProd, etxQtdStock, etxQtdAlertStock, etxQtdPrateleira, etxAlertQtdPrateleira;
    private ProductItem currentItem;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);
        btnVoltar = findViewById(R.id.btn_voltar);
        btnSalvarCad = findViewById(R.id.btn_salvar_cad);
        etxNomeProd = findViewById(R.id.etx_nome_prod);
        etxQtdStock = findViewById(R.id.etx_qtd_stock);
        etxQtdAlertStock = findViewById(R.id.etx_qtd_alert_stock);
        etxQtdPrateleira = findViewById(R.id.etx_qtd_prateleira);
        etxAlertQtdPrateleira = findViewById(R.id.etx_alert_qtd_prateleira);

        if (Api.hasCurrentItem()) {
            currentItem = Api.popCurrentItem();
            updateTextViewFields();
        }
        handler = new Handler();
        Activity activity = this;

        btnVoltar.setOnClickListener(view -> {
            if (currentItem == null) {
                System.out.println("Voltando para o Home Administrador...");
                Intent intent = new Intent(activity, HomeAdmActivity.class);
                startActivity(intent);
            } else {
                System.out.println("Voltando para tela de detalhes do administrador...");
                Api.putCurrentItem(currentItem);
                Intent intent = new Intent(activity, InfoProdutoAdmActivity.class);
                startActivity(intent);
            }
        });
        btnSalvarCad.setOnClickListener(view -> {
            System.out.println("Salvando produto...");
            String name = etxNomeProd.getText().toString();
            String countStock = etxQtdStock.getText().toString();
            String countStockAlert = etxQtdAlertStock.getText().toString();
            String countStand = etxQtdPrateleira.getText().toString();
            String countStandAlert = etxAlertQtdPrateleira.getText().toString();

            Toast errorToast;
            if (name.trim().isEmpty()) {
                errorToast = Toast.makeText(activity, "Por favor, insira um nome para o produto", Toast.LENGTH_LONG);
            } else if (countStock.trim().isEmpty()) {
                errorToast = Toast.makeText(activity, "Por favor, insira a quantidade em estoque", Toast.LENGTH_LONG);
            } else if (countStockAlert.trim().isEmpty()) {
                errorToast = Toast.makeText(activity, "Por favor, insira um alerta para o estoque", Toast.LENGTH_LONG);
            } else if (countStand.trim().isEmpty()) {
                errorToast = Toast.makeText(activity, "Por favor, insira a quantidade em prateleiras", Toast.LENGTH_LONG);
            } else  if (countStandAlert.trim().isEmpty()) {
                errorToast = Toast.makeText(activity, "Por favor, insira um alerta para as prateleiras", Toast.LENGTH_LONG);
            } else {
                etxNomeProd.clearFocus();
                etxQtdStock.clearFocus();
                etxQtdAlertStock.clearFocus();
                etxQtdPrateleira.clearFocus();
                etxAlertQtdPrateleira.clearFocus();
                ProductItem item = new ProductItem(
                        name,
                        Integer.parseInt(countStock),
                        Integer.parseInt(countStockAlert),
                        Integer.parseInt(countStand),
                        Integer.parseInt(countStandAlert)
                );
                if (currentItem != null) {
                    ProductItem modifiedItem = item.withId(currentItem.id);
                    System.out.println("Modificando produto " + currentItem.name);
                    Api.modifyItem(modifiedItem, new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) {
                            if (response.isSuccessful()) {
                                handler.post(() -> {
                                    Toast successToast = Toast.makeText(activity, "Produto \"" + name + "\" modificado com sucesso!", Toast.LENGTH_LONG);
                                    successToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
                                    successToast.show();
                                    System.out.println("Voltando à tela de detalhes do administrador...");
                                    Api.putCurrentItem(modifiedItem);
                                    Intent intent = new Intent(activity, InfoProdutoAdmActivity.class);
                                    startActivity(intent);
                                });
                            } else {
                                System.out.println("Tudo errado: " + response);
                                handler.post(() -> {
                                    Toast failureToast = Toast.makeText(activity, "Falha na modificação do produto", Toast.LENGTH_LONG);
                                    failureToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
                                    failureToast.show();
                                });
                            }
                        }
                    });
                    return;
                }
                System.out.println("Registrando produto " + name);
                final boolean[] wasSuccessful = {false};
                Semaphore semaphore = new Semaphore(1);
                semaphore.acquireUninterruptibly();
                Api.registerProduct(item, new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    wasSuccessful[0] = true;
                                } else {
                                    System.out.println("Tudo errado: " + response);
                                }
                                semaphore.release();
                            }
                        }
                );
                semaphore.acquireUninterruptibly();
                semaphore.release();
                if (wasSuccessful[0]) { // Success
                    etxNomeProd.getText().clear();
                    etxQtdStock.getText().clear();
                    etxQtdAlertStock.getText().clear();
                    etxQtdPrateleira.getText().clear();
                    etxAlertQtdPrateleira.getText().clear();
                    Toast successToast = Toast.makeText(activity, "Produto \"" + name + "\" criado com sucesso!", Toast.LENGTH_LONG);
                    successToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
                    successToast.show();
                } else { // Error
                    Toast failureToast = Toast.makeText(activity, "Falha na criação do produto", Toast.LENGTH_LONG);
                    failureToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
                    failureToast.show();
                }
                return;
            }
            errorToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
            errorToast.show();
        });

    }

    private void updateTextViewFields() {
        etxNomeProd.setText(currentItem.name);
        etxQtdStock.setText(String.valueOf(currentItem.countStock));
        etxQtdAlertStock.setText(String.valueOf(currentItem.countStockAlert));
        etxQtdPrateleira.setText(String.valueOf(currentItem.countStand));
        etxAlertQtdPrateleira.setText(String.valueOf(currentItem.countStandAlert));
    }
}