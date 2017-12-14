package com.example.android.coffeeshopapp.views;


import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public interface TransactionView extends BaseView {

    void transactionSuccess(PurchaseTransactionEntity transactionEntity);

    void transactionFailed(String message);

    void reloadBalance(double balance);
}
