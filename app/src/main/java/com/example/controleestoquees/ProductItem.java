package com.example.controleestoquees;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;
import java.util.UUID;

public class ProductItem {
    public final UUID id;
    @SerializedName("nome")
    public final String name;
    @SerializedName("qtd")
    public final int countStock;
    @SerializedName("qtd_alert_stock")
    public final int countStockAlert;
    @SerializedName("qtd_stand")
    public final int countStand;
    @SerializedName("qtd_alert_stand")
    public final int countStandAlert;

    public ProductItem(String name, int countStock, int countStockAlert, int countStand, int countStandAlert) {
        this.id = null;
        this.name = name;
        this.countStock = countStock;
        this.countStockAlert = countStockAlert;
        this.countStand = countStand;
        this.countStandAlert = countStandAlert;
    }

    public ProductItem(UUID id, String name, int countStock, int countStockAlert, int countStand, int countStandAlert) {
        this.id = id;
        this.name = name;
        this.countStock = countStock;
        this.countStockAlert = countStockAlert;
        this.countStand = countStand;
        this.countStandAlert = countStandAlert;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static ProductItem fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ProductItem.class);
    }

    public ProductItem withId(UUID id) {
        return new ProductItem(
                id,
                name,
                countStock,
                countStockAlert,
                countStand,
                countStandAlert
        );
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
