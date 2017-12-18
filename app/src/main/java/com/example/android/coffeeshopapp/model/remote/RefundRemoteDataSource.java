package com.example.android.coffeeshopapp.model.remote;

import com.example.android.coffeeshopapp.api.CoffeeShopApi;
import com.example.android.coffeeshopapp.model.IRefundDataSource;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public class RefundRemoteDataSource implements IRefundDataSource {

    CoffeeShopApi coffeeShopApi;

    public RefundRemoteDataSource(CoffeeShopApi coffeeShopApi) {
        this.coffeeShopApi = coffeeShopApi;
    }

    @Override
    public Observable<ResponseBody> refundTransaction(String cardId, long purchaseId, double price) {
        return coffeeShopApi.refundTransaction(cardId, purchaseId, price);
    }
}
