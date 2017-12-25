package com.example.android.coffeeshopapp.model;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by dev_serhii on 25.12.2017.
 */

public interface IZXReportDataSource {

    Observable<ResponseBody> getXReport(String employeeId);

    Observable<ResponseBody> getZReport(String employeeId);
}
