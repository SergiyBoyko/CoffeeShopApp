package com.example.android.coffeeshopapp.model;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public interface ITransactionDataSource {

    Observable<ResponseBody> confirmTransaction(long id, double price);

    Observable<ResponseBody> getBalance(long id);
}
