package com.example.android.coffeeshopapp.model;

import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public interface IRefundDataSource {

    Observable<ResponseBody> checkPin(String pin);

    Observable<PurchaseTransactionEntity> refundTransaction(String cardId, long purchaseId, double price, String employeeId);
}
