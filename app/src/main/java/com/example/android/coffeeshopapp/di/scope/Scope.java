package com.example.android.coffeeshopapp.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by dev_serhii on 05.12.2017.
 */

@javax.inject.Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {

    String value() default "";

}
