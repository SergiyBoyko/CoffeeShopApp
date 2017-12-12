package com.example.android.coffeeshopapp.views;

import com.example.android.coffeeshopapp.model.entities.ResponseEntity;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public interface UserInfoView extends BaseView {

    void onVerifySuccess(ResponseEntity responseEntity);

    void onVerifyFailed(String message);
}