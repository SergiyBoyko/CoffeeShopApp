package com.example.android.coffeeshopapp.presenters;

import com.example.android.coffeeshopapp.model.ITransactionsListDataSource;
import com.example.android.coffeeshopapp.utils.rx.RxErrorAction;
import com.example.android.coffeeshopapp.utils.rx.RxRetryWithDelay;
import com.example.android.coffeeshopapp.views.TransactionListView;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public class TransactionListPresenter extends BasePresenter<TransactionListView> {

    private final ITransactionsListDataSource transactionsListDataSource;

    public TransactionListPresenter(ITransactionsListDataSource transactionsListDataSource) {
        this.transactionsListDataSource = transactionsListDataSource;
    }

    public void getAllPurchases(long id) {
        addSubscription(transactionsListDataSource.getAllPurchases(id)
                .retryWhen(new RxRetryWithDelay())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getView()::showTransactionList, new RxErrorAction(getView().getContext())));
    }

    public void getAllPurchasesForDay() {
        addSubscription(transactionsListDataSource.getAllPurchasesForDay()
                .retryWhen(new RxRetryWithDelay())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getView()::showTransactionList, new RxErrorAction(getView().getContext())));

    }
}
