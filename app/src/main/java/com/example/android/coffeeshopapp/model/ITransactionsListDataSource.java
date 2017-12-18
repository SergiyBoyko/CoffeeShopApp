package com.example.android.coffeeshopapp.model;

import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public interface ITransactionsListDataSource {

    Observable<List<PurchaseTransactionEntity>> getAllPurchases(String id);

    Observable<List<PurchaseTransactionEntity>> getAllPurchasesForDay();

}
