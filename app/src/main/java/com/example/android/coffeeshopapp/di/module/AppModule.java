package com.example.android.coffeeshopapp.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dev_serhii on 05.12.2017.
 */

@Module
public final class AppModule {

    private Context context;

    public AppModule(@NonNull Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    SharedPreferences providePreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Executor provideExecutor() {
        return Executors.newCachedThreadPool();
    }

}
