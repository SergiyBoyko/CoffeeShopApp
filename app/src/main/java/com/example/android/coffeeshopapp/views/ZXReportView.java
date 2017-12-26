package com.example.android.coffeeshopapp.views;

import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

import java.util.List;

/**
 * Created by dev_serhii on 25.12.2017.
 */

public interface ZXReportView extends BaseView {

    void showZReport(List<PurchaseTransactionEntity> transactionEntities, long lastTimeUpdate);

    void showXReport(List<PurchaseTransactionEntity> transactionEntities, long lastTimeUpdate);

}
