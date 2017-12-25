package com.example.android.coffeeshopapp.views;

import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public interface RefundView extends BaseView {

    void onRefundSuccess(PurchaseTransactionEntity transactionEntity);

    void onRefundFailed(String message);

    void onPinVerifySuccess(String message);

}
