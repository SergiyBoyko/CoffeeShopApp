package com.example.android.coffeeshopapp.views;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public interface RefundView extends BaseView {

    void onRefundSuccess(String message);

    void onRefundFailed(String message);

}
