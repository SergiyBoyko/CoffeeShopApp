package com.example.android.coffeeshopapp.di.component;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.coffeeshopapp.di.module.ApiModule;
import com.example.android.coffeeshopapp.di.module.AppModule;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by dev_serhii on 05.12.2017.
 */

@Singleton
@Component(modules = {
        AppModule.class,
        ApiModule.class
})
public interface AppComponent extends ApiComponent {

    Context context();

    SharedPreferences preferences();

    Executor executor();

}
