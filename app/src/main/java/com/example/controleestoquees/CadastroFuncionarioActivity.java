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

public class CadastroFuncionarioActivity extends AppCompatActivity {

    private Button btnVoltar, btnSalvarCad;
    private TextView etxNomeProd, etxQtdStock, etxQtdAlertStock, etxQtdPrateleira;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_funcionario);
        btnVoltar = findViewById(R.id.btn_voltar);
        btnSalvarCad = findViewById(R.id.btn_salvar_cad);
        etxNomeProd = findViewById(R.id.etx_nome_prod);             // Nome
        etxQtdStock = findViewById(R.id.etx_qtd_stock);             // Usuário
        etxQtdAlertStock = findViewById(R.id.etx_qtd_alert_stock);  // Cargo
        etxQtdPrateleira = findViewById(R.id.etx_qtd_prateleira);   // Senha

        handler = new Handler();
        Activity activity = this;

        btnVoltar.setOnClickListener(view -> {
            System.out.println("Voltando ao Home de administrador...");
            Intent intent = new Intent(activity, HomeAdmActivity.class);
            startActivity(intent);
        });

        btnSalvarCad.setOnClickListener(view -> {
            String workerName = etxNomeProd.getText().toString();
            String workerUsername = etxQtdStock.getText().toString();
            String workerRole = etxQtdAlertStock.getText().toString();
            String workerPassword = etxQtdPrateleira.getText().toString();

            workerName = workerName.trim();
            workerUsername = workerUsername.trim();
            workerRole = workerRole.trim();
            workerPassword = workerPassword.trim();

            Toast failureToast;
            if (workerName.isEmpty()) {
                failureToast = Toast.makeText(activity, "Insira o nome do funcionário!", Toast.LENGTH_LONG);
            } else if (workerUsername.isEmpty()) {
                failureToast = Toast.makeText(activity, "Insira o nome de usuário do funcionário!", Toast.LENGTH_LONG);
            } else if (workerRole.isEmpty()) {
                failureToast = Toast.makeText(activity, "Insira o cargo do funcionário!", Toast.LENGTH_LONG);
            } else if (workerPassword.isEmpty()) {
                failureToast = Toast.makeText(activity, "Insira a senha do funcionário!", Toast.LENGTH_LONG);
            } else {
                Api.registerUser(workerUsername, workerName, workerPassword, workerRole, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        handler.post(() -> {
                            if (response.isSuccessful()) {
                                Toast successToast = Toast.makeText(activity, "Usuário criado com sucesso!", Toast.LENGTH_LONG);
                                successToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
                                successToast.show();
                                etxNomeProd.setText("");
                                etxQtdStock.setText("");
                                etxQtdAlertStock.setText("");
                                etxQtdPrateleira.setText("");
                            } else {
                                System.out.println("Tudo errado: " + response);
                                Toast failureToast = Toast.makeText(activity, "Erro na criação de usuário", Toast.LENGTH_LONG);
                                failureToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
                                failureToast.show();
                            }
                        });
                    }
                });
                return;
            }
            failureToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
            failureToast.show();
        });
    }
}