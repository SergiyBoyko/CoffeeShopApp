package com.example.android.coffeeshopapp;

import android.support.multidex.MultiDexApplication;

import com.example.android.coffeeshopapp.di.component.AppComponent;
import com.example.android.coffeeshopapp.di.component.DaggerAppComponent;
import com.example.android.coffeeshopapp.di.module.ApiModule;
import com.example.android.coffeeshopapp.di.module.AppModule;

/**
 * Created by dev_serhii on 05.12.2017.
 */

public class AppCoffeeShop extends MultiDexApplication{
    private AppComponent appComponent;

    public AppCoffeeShop() {
        super();

        appComponent = DaggerAppComponent.builder()
                .apiModule(new ApiModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent appComponent() {
        return appComponent;
    }
}
