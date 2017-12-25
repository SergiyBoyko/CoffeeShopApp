package com.example.android.coffeeshopapp.presenters;

import com.example.android.coffeeshopapp.model.IZXReportDataSource;
import com.example.android.coffeeshopapp.model.remote.ZXReportRemoteDataSource;
import com.example.android.coffeeshopapp.views.ZXReportView;

/**
 * Created by dev_serhii on 25.12.2017.
 */

public class ZXReportPresenter extends BasePresenter<ZXReportView> {

    private final IZXReportDataSource reportDataSource;

    public ZXReportPresenter(IZXReportDataSource reportDataSource) {
        this.reportDataSource = reportDataSource;
    }
}
