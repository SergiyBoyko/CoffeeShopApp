package com.example.android.coffeeshopapp.presenters;

import com.example.android.coffeeshopapp.model.IRefundDataSource;
import com.example.android.coffeeshopapp.utils.rx.RxRetryWithDelay;
import com.example.android.coffeeshopapp.views.RefundView;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public class RefundPresenter extends BasePresenter<RefundView> {

    private final IRefundDataSource refundDataSource;

    public RefundPresenter(IRefundDataSource refundDataSource) {
        this.refundDataSource = refundDataSource;
    }

    public void tryToRefund(String pin, String cardId, long purchaseId, double price, String employeeId) {
        addSubscription(refundDataSource.checkPin(pin)
                .retryWhen(new RxRetryWithDelay())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                    try {
                        getView().onPinVerifySuccess(responseBody.string());
                        refundTransaction(cardId, purchaseId, price, employeeId);
                    } catch (IOException e) {
                        getView().onRefundFailed("null");
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
                        getView().onRefundFailed(message);
                    }
                })
        );
    }

    public void refundTransaction(String cardId, long purchaseId, double price, String employeeId) {
        addSubscription(refundDataSource.refundTransaction(cardId, purchaseId, price, employeeId)
                .retryWhen(new RxRetryWithDelay())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getView()::onRefundSuccess, throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        String message = httpException.getMessage();
                        try {
                            message = httpException.response().errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        getView().onRefundFailed(message);
                    }
                })
        );
    }

}
