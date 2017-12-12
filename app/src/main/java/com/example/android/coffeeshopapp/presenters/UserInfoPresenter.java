package com.example.android.coffeeshopapp.presenters;

import com.example.android.coffeeshopapp.model.IUserInfoDataSource;
import com.example.android.coffeeshopapp.utils.rx.RxErrorAction;
import com.example.android.coffeeshopapp.utils.rx.RxRetryWithDelay;
import com.example.android.coffeeshopapp.views.UserInfoView;

import rx.Observable;
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

    public void getUserData(int id) {
        addSubscription(userInfoDataSource.getUserData(id)
                .retryWhen(new RxRetryWithDelay())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getView()::onVerifySuccess, new RxErrorAction(getView().getContext()))
        );
    }

}
