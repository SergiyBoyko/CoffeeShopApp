package com.example.android.coffeeshopapp.model;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public interface IRefundDataSource {

    Observable<ResponseBody> refundTransaction(long cardId, long purchaseId, double price);
}
