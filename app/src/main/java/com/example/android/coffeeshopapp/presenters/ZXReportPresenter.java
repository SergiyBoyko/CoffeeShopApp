package com.example.android.coffeeshopapp.presenters;

import com.example.android.coffeeshopapp.model.IZXReportDataSource;
import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;
import com.example.android.coffeeshopapp.model.remote.ZXReportRemoteDataSource;
import com.example.android.coffeeshopapp.utils.rx.RxErrorAction;
import com.example.android.coffeeshopapp.utils.rx.RxRetryWithDelay;
import com.example.android.coffeeshopapp.views.ZXReportView;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dev_serhii on 25.12.2017.
 */

public class ZXReportPresenter extends BasePresenter<ZXReportView> {

    private final IZXReportDataSource reportDataSource;

    public ZXReportPresenter(IZXReportDataSource reportDataSource) {
        this.reportDataSource = reportDataSource;
    }

    public void getXReportWithLastTimeUpdate(String employeeId) {
        addSubscription(reportDataSource.getLastTimeUpdated(employeeId)
                .retryWhen(new RxRetryWithDelay())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                            long latTimeUpdate = 0;
                            try {
                                latTimeUpdate = Long.parseLong(responseBody.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                getXReport(employeeId, latTimeUpdate);
                            }
                        },
                        new RxErrorAction(getView().getContext())));
    }

    private void getXReport(String employeeId, long lastTimeUpdate) {
        addSubscription(reportDataSource.getXReport(employeeId)
                .retryWhen(new RxRetryWithDelay())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactionEntities ->
                                getView().showXReport(transactionEntities, lastTimeUpdate),
                        new RxErrorAction(getView().getContext())));
    }

    public void getZReportWithLastTimeUpdate(String employeeId) {
        addSubscription(reportDataSource.getLastTimeUpdated(employeeId)
                .retryWhen(new RxRetryWithDelay())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                            long latTimeUpdate = 0;
                            try {
                                latTimeUpdate = Long.parseLong(responseBody.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                getZReport(employeeId, latTimeUpdate);
                            }
                        },
                        new RxErrorAction(getView().getContext())));
    }

    private void getZReport(String employeeId, long lastTimeUpdate) {
        addSubscription(reportDataSource.getZReport(employeeId)
                .retryWhen(new RxRetryWithDelay())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactionEntities ->
                                getView().showZReport(transactionEntities, lastTimeUpdate),
                        new RxErrorAction(getView().getContext())));
    }

}
