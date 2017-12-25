package com.example.android.coffeeshopapp.model.remote;

import com.example.android.coffeeshopapp.api.CoffeeShopApi;
import com.example.android.coffeeshopapp.model.ITransactionDataSource;
import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public class TransactionRemoteDataSource implements ITransactionDataSource {

    private CoffeeShopApi coffeeShopApi;

    public TransactionRemoteDataSource(CoffeeShopApi coffeeShopApi) {
        this.coffeeShopApi = coffeeShopApi;
    }

    @Override
    public Observable<PurchaseTransactionEntity> confirmTransaction(String id, double price, String employeeId) {
        return coffeeShopApi.confirmTransaction(id, price, employeeId);
    }

    @Override
    public Observable<ResponseBody> getBalance(String id) {
        return coffeeShopApi.getBalance(id);
    }
}
