package com.example.android.coffeeshopapp.di.module;

import com.example.android.coffeeshopapp.api.CoffeeShopApi;
import com.example.android.coffeeshopapp.common.Constants;
import com.example.android.coffeeshopapp.model.IRefundDataSource;
import com.example.android.coffeeshopapp.model.ITransactionDataSource;
import com.example.android.coffeeshopapp.model.ITransactionsListDataSource;
import com.example.android.coffeeshopapp.model.IUserInfoDataSource;
import com.example.android.coffeeshopapp.model.IZXReportDataSource;
import com.example.android.coffeeshopapp.model.remote.RefundRemoteDataSource;
import com.example.android.coffeeshopapp.model.remote.TransactionRemoteDataSource;
import com.example.android.coffeeshopapp.model.remote.TransactionsListRemoteDataSource;
import com.example.android.coffeeshopapp.model.remote.UserInfoRemoteDataSource;
import com.example.android.coffeeshopapp.model.remote.ZXReportRemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dev_serhii on 05.12.2017.
 */

@Module
public class ApiModule {

    @Provides
    @Singleton
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.COFFEE_SHOP_TEST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    IUserInfoDataSource provideUserInfoDataSource(Retrofit retrofit) {
        return new UserInfoRemoteDataSource(retrofit.create(CoffeeShopApi.class));
    }

    @Provides
    @Singleton
    ITransactionDataSource provideTransactionDataSource(Retrofit retrofit) {
        return new TransactionRemoteDataSource(retrofit.create(CoffeeShopApi.class));
    }

    @Provides
    @Singleton
    ITransactionsListDataSource provideTransactionListDataSource(Retrofit retrofit) {
        return new TransactionsListRemoteDataSource(retrofit.create(CoffeeShopApi.class));
    }

    @Provides
    @Singleton
    IRefundDataSource provideRefundDataSource(Retrofit retrofit) {
        return new RefundRemoteDataSource(retrofit.create(CoffeeShopApi.class));
    }

    @Provides
    @Singleton
    IZXReportDataSource provideIZXReportDataSource(Retrofit retrofit) {
        return new ZXReportRemoteDataSource(retrofit.create(CoffeeShopApi.class));
    }

}
