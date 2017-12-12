package com.example.android.coffeeshopapp.views;


/**
 * Created by dev_serhii on 12.12.2017.
 */

public interface TransactionView extends BaseView {

    void transactionSuccess(String message);

    void transactionFailed(String message);

    void reloadBalance(double balance);
}
