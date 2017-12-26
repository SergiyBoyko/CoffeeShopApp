package com.example.android.coffeeshopapp.di.module;

import com.example.android.coffeeshopapp.di.scope.Scope;
import com.example.android.coffeeshopapp.di.scope.Scopes;
import com.example.android.coffeeshopapp.model.IRefundDataSource;
import com.example.android.coffeeshopapp.model.ITransactionDataSource;
import com.example.android.coffeeshopapp.model.ITransactionsListDataSource;
import com.example.android.coffeeshopapp.model.IUserInfoDataSource;
import com.example.android.coffeeshopapp.model.IZXReportDataSource;
import com.example.android.coffeeshopapp.presenters.RefundPresenter;
import com.example.android.coffeeshopapp.presenters.TransactionListPresenter;
import com.example.android.coffeeshopapp.presenters.TransactionPresenter;
import com.example.android.coffeeshopapp.presenters.UserInfoPresenter;
import com.example.android.coffeeshopapp.presenters.ZXReportPresenter;

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

    @Provides
    @Scope(Scopes.VIEW)
    public TransactionPresenter provideTransactionPresenter(ITransactionDataSource transactionDataSource) {
        return new TransactionPresenter(transactionDataSource);
    }

    @Provides
    @Scope(Scopes.VIEW)
    public TransactionListPresenter provideTransactionlistPresenter(ITransactionsListDataSource transactionsListDataSource) {
        return new TransactionListPresenter(transactionsListDataSource);
    }

    @Provides
    @Scope(Scopes.VIEW)
    public RefundPresenter provideRefundPresenter(IRefundDataSource refundDataSource) {
        return new RefundPresenter(refundDataSource);
    }

    @Provides
    @Scope(Scopes.VIEW)
    public ZXReportPresenter provideZXReportPresenter(IZXReportDataSource reportDataSource) {
        return new ZXReportPresenter(reportDataSource);
    }

}
