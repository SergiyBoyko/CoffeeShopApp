package com.example.android.coffeeshopapp.model.remote;

import com.example.android.coffeeshopapp.api.CoffeeShopApi;
import com.example.android.coffeeshopapp.model.IRefundDataSource;
import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public class RefundRemoteDataSource implements IRefundDataSource {

    private CoffeeShopApi coffeeShopApi;

    public RefundRemoteDataSource(CoffeeShopApi coffeeShopApi) {
        this.coffeeShopApi = coffeeShopApi;
    }

    @Override
    public Observable<ResponseBody> checkPin(String pin) {
        return coffeeShopApi.checkPin(pin);
    }

    @Override
    public Observable<PurchaseTransactionEntity> refundTransaction(String cardId,
                                                                   long purchaseId,
                                                                   double price,
                                                                   String employeeId) {
        return coffeeShopApi.refundTransaction(cardId, purchaseId, price, employeeId);
    }
}
