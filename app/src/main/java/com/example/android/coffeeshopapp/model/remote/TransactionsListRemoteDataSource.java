package com.example.android.coffeeshopapp.model.remote;

import com.example.android.coffeeshopapp.api.CoffeeShopApi;
import com.example.android.coffeeshopapp.model.ITransactionsListDataSource;
import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public class TransactionsListRemoteDataSource implements ITransactionsListDataSource {

    private CoffeeShopApi coffeeShopApi;

    public TransactionsListRemoteDataSource(CoffeeShopApi coffeeShopApi) {
        this.coffeeShopApi = coffeeShopApi;
    }

    @Override
    public Observable<List<PurchaseTransactionEntity>> getAllPurchases(String id, String employeeId) {
        return coffeeShopApi.getAllPurchases(id, employeeId);
    }

    @Override
    public Observable<List<PurchaseTransactionEntity>> getAllPurchasesForDay(String employeeId) {
        return coffeeShopApi.getAllPurchasesForDay(employeeId);
    }
}
