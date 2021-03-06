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
    Observable<ResponseEntity> getUserData(@Query("cardId") String cardId);

    @GET("/user/getBalance")
    Observable<ResponseBody> getBalance(@Query("cardId") String cardId);

    @GET("/purchase/getAllPurchases")
    Observable<List<PurchaseTransactionEntity>> getAllPurchases(@Query("cardId") String cardId,
                                                                @Query("employeeId") String employeeId);

    @GET("/purchase/getAllPurchasesForDay")
    Observable<List<PurchaseTransactionEntity>> getAllPurchasesForDay(@Query("employeeId") String employeeId);

    @POST("/purchase/refund")
    Observable<PurchaseTransactionEntity> refundTransaction(@Query("cardId") String cardId,
                                               @Query("purchaseId") long purchaseId,
                                               @Query("price") double price,
                                               @Query("employeeId") String employeeId);

    @POST("/purchase/buy")
    Observable<PurchaseTransactionEntity> confirmTransaction(@Query("cardId") String cardId,
                                                             @Query("price") double price,
                                                             @Query("employeeId") String employeeId);

    @GET("/purchase/confirmPass")
    Observable<ResponseBody> checkPin(@Query("password") String pin);

    @GET("/purchase/getAllPurchasesXReport")
    Observable<List<PurchaseTransactionEntity>> getXReport(@Query("employeeId") String employeeId);

    @GET("/purchase/getAllPurchasesZReport")
    Observable<List<PurchaseTransactionEntity>> getZReport(@Query("employeeId") String employeeId);

    //https://coffee-shop-test.herokuapp.com/purchase/getLastTimeUpdated?employeeId=1111
    @GET("/purchase/getLastTimeUpdated")
    Observable<ResponseBody> getLastTimeUpdated(@Query("employeeId") String employeeId);
}
