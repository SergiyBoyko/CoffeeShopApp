package com.example.android.coffeeshopapp.presenters;

import com.example.android.coffeeshopapp.views.BaseView;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dev_serhii on 06.12.2017.
 */

public class BasePresenter<T extends BaseView> {

    private T view;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    public void destroy() {
        compositeSubscription.clear();
    }

    protected Subscription addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
        return subscription;
    }

}
