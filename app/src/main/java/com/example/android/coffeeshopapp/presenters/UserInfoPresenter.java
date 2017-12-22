package com.example.android.coffeeshopapp.presenters;

import android.widget.Toast;

import com.example.android.coffeeshopapp.model.IUserInfoDataSource;
import com.example.android.coffeeshopapp.utils.rx.RxRetryWithDelay;
import com.example.android.coffeeshopapp.views.UserInfoView;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public class UserInfoPresenter extends BasePresenter<UserInfoView> {

    private final IUserInfoDataSource userInfoDataSource;

    public UserInfoPresenter(IUserInfoDataSource userInfoDataSource) {
        this.userInfoDataSource = userInfoDataSource;
    }

    public void getUserData(String cardId) {
        addSubscription(userInfoDataSource.getUserData(cardId)
                .retryWhen(new RxRetryWithDelay())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getView()::onVerifySuccess, throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        String message = httpException.getMessage();
                        try {
                            message = httpException.response().errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        getView().onVerifyFailed(message);
                    }
                })
        );
    }

}
