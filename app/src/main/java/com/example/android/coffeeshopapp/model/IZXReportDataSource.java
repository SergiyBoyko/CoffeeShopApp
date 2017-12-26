package com.example.android.coffeeshopapp.model;

import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by dev_serhii on 25.12.2017.
 */

public interface IZXReportDataSource {

    Observable<List<PurchaseTransactionEntity>> getXReport(String employeeId);

    Observable<List<PurchaseTransactionEntity>> getZReport(String employeeId);

    Observable<ResponseBody> getLastTimeUpdated(String employeeId);
}
