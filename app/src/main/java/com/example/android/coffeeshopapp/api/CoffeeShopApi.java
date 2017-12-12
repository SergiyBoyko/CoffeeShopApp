package com.example.android.coffeeshopapp.api;

import com.example.android.coffeeshopapp.model.entities.ResponseEntity;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dev_serhii on 05.12.2017.
 */

public interface CoffeeShopApi {

    @GET("/user/getUser")
    Observable<ResponseEntity> getUserData(@Query("userId") long id);

    @GET("/user/getBalance")
    Observable<ResponseBody> getBalance(@Query("card_id") long id);

    @POST("/purchase/buy")
    Observable<ResponseBody> confirmTransaction(@Query("card_id") long id, @Query("price") double price);
}
