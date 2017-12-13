package com.example.android.coffeeshopapp.views;

import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

import java.util.List;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public interface TransactionListView extends BaseView {

    void showTransactionList(List<PurchaseTransactionEntity> list);

}
