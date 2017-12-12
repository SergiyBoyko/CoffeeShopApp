package com.example.android.coffeeshopapp.di.component;

import com.example.android.coffeeshopapp.di.module.PresentersModule;
import com.example.android.coffeeshopapp.di.scope.Scope;
import com.example.android.coffeeshopapp.di.scope.Scopes;
import com.example.android.coffeeshopapp.ui.activities.MainActivity;

import dagger.Component;

/**
 * Created by dev_serhii on 05.12.2017.
 */

@Scope(Scopes.VIEW)
@Component(
        modules = {PresentersModule.class},
        dependencies = {AppComponent.class}
)
public interface PresentersComponent {

    void inject(MainActivity mainActivity);

}
