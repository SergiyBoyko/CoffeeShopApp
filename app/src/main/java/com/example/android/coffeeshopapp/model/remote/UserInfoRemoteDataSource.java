package com.example.android.coffeeshopapp.model.remote;

import com.example.android.coffeeshopapp.api.CoffeeShopApi;
import com.example.android.coffeeshopapp.model.IUserInfoDataSource;
import com.example.android.coffeeshopapp.model.entities.ResponseEntity;

import rx.Observable;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public class UserInfoRemoteDataSource implements IUserInfoDataSource {

    CoffeeShopApi coffeeShopApi;

    public UserInfoRemoteDataSource(CoffeeShopApi coffeeShopApi) {
        this.coffeeShopApi = coffeeShopApi;
    }

    @Override
    public Observable<ResponseEntity> getUserData(String cardId) {
        return coffeeShopApi.getUserData(cardId);
    }
}
