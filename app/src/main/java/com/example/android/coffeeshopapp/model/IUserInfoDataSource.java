package com.example.android.coffeeshopapp.model;

import com.example.android.coffeeshopapp.model.entities.ResponseEntity;

import rx.Observable;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public interface IUserInfoDataSource {

    Observable<ResponseEntity> getUserData(String cardId);

}
