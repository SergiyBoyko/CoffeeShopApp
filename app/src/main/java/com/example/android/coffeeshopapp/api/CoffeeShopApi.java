package com.example.android.coffeeshopapp.api;

import com.example.android.coffeeshopapp.model.entities.ResponseEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dev_serhii on 05.12.2017.
 */

public interface CoffeeShopApi {

    @GET("/user/getUser")
    Observable<ResponseEntity> getUserData(@Query("userId") int id);

}
