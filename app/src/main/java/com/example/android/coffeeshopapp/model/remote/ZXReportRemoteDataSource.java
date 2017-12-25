package com.example.android.coffeeshopapp.model.remote;

import com.example.android.coffeeshopapp.api.CoffeeShopApi;
import com.example.android.coffeeshopapp.model.IZXReportDataSource;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by dev_serhii on 25.12.2017.
 */

public class ZXReportRemoteDataSource implements IZXReportDataSource {

    private CoffeeShopApi coffeeShopApi;

    public ZXReportRemoteDataSource(CoffeeShopApi coffeeShopApi) {
        this.coffeeShopApi = coffeeShopApi;
    }

    @Override
    public Observable<ResponseBody> getXReport(String employeeId) {
        return coffeeShopApi.getXReport(employeeId);
    }

    @Override
    public Observable<ResponseBody> getZReport(String employeeId) {
        return coffeeShopApi.getZReport(employeeId);
    }
    
}
