package com.example.android.coffeeshopapp.presenters;

import com.example.android.coffeeshopapp.model.ITransactionDataSource;
import com.example.android.coffeeshopapp.utils.rx.RxRetryWithDelay;
import com.example.android.coffeeshopapp.views.TransactionView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public class TransactionPresenter extends BasePresenter<TransactionView> {

    private final ITransactionDataSource transactionDataSource;

    public TransactionPresenter(ITransactionDataSource transactionDataSource) {
        this.transactionDataSource = transactionDataSource;
    }

    public void confirmTransaction(long id, double price) {
        addSubscription(transactionDataSource.confirmTransaction(id, price)
                .retryWhen(new RxRetryWithDelay())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getView()::transactionSuccess, throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        String message = httpException.getMessage();
                        try {
                            message = httpException.response().errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        getView().transactionFailed(message);
                    }
                })
        );

    }

    public void getBalance(long id) {
        addSubscription(transactionDataSource.getBalance(id)
                .retryWhen(new RxRetryWithDelay())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                    try {
                        getView().reloadBalance(Double.parseDouble(responseBody.string()));
                    } catch (IOException e) {
                        getView().transactionFailed("null");
                        e.printStackTrace();
                    }
                }, throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        String message = httpException.getMessage();
                        try {
                            message = httpException.response().errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        getView().transactionFailed(message);
                    }
                })
        );
    }
}
