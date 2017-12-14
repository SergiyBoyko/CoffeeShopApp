package com.example.android.coffeeshopapp.api;

import com.example.android.coffeeshopapp.model.entities.ResponseEntity;
import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

import java.util.List;

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

    @GET("/purchase/getAllPurchases")
    Observable<List<PurchaseTransactionEntity>> getAllPurchases(@Query("card_id") long id);

    @GET("/purchase/getAllPurchasesForDay")
    Observable<List<PurchaseTransactionEntity>> getAllPurchasesForDay();

    @POST("/purchase/refund")
    Observable<ResponseBody> refundTransaction(@Query("card_id") long cardId,
                                               @Query("purchaseId") long purchaseId,
                                               @Query("price") double price);

    @POST("/purchase/buy")
    Observable<PurchaseTransactionEntity> confirmTransaction(@Query("card_id") long id, @Query("price") double price);
}
