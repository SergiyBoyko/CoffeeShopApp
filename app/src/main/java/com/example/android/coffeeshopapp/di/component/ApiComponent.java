package com.example.android.coffeeshopapp.di.component;

import com.example.android.coffeeshopapp.model.IRefundDataSource;
import com.example.android.coffeeshopapp.model.ITransactionDataSource;
import com.example.android.coffeeshopapp.model.ITransactionsListDataSource;
import com.example.android.coffeeshopapp.model.IUserInfoDataSource;

import retrofit2.Retrofit;

/**
 * Created by dev_serhii on 05.12.2017.
 */

public interface ApiComponent {

    Retrofit retrofit();

    IUserInfoDataSource userInfoDataSource();

    ITransactionDataSource transactionDataSource();

    ITransactionsListDataSource transactionsListDataSource();

    IRefundDataSource refundDataSource();

}
