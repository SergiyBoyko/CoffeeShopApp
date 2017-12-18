package com.example.android.coffeeshopapp.model;

import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public interface ITransactionDataSource {

    Observable<PurchaseTransactionEntity> confirmTransaction(String id, double price);

    Observable<ResponseBody> getBalance(String id);
}
