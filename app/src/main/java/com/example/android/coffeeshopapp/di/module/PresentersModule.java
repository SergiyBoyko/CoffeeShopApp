package com.example.android.coffeeshopapp.di.module;

import com.example.android.coffeeshopapp.di.scope.Scope;
import com.example.android.coffeeshopapp.di.scope.Scopes;
import com.example.android.coffeeshopapp.model.IUserInfoDataSource;
import com.example.android.coffeeshopapp.presenters.UserInfoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dev_serhii on 05.12.2017.
 */

@Module
public class PresentersModule {

    @Provides
    @Scope(Scopes.VIEW)
    public UserInfoPresenter provideUserInfoPresenter(IUserInfoDataSource userInfoDataSource) {
        return new UserInfoPresenter(userInfoDataSource);
    }

}
