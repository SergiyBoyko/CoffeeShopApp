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

    CoffeeShopApi coffeeShopApi;

    public TransactionRemoteDataSource(CoffeeShopApi coffeeShopApi) {
        this.coffeeShopApi = coffeeShopApi;
    }

    @Override
    public Observable<PurchaseTransactionEntity> confirmTransaction(long id, double price) {
        return coffeeShopApi.confirmTransaction(id, price);
    }

    @Override
    public Observable<ResponseBody> getBalance(long id) {
        return coffeeShopApi.getBalance(id);
    }
}
