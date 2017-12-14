package com.example.android.coffeeshopapp.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.coffeeshopapp.AppCoffeeShop;
import com.example.android.coffeeshopapp.R;
import com.example.android.coffeeshopapp.common.Constants;
import com.example.android.coffeeshopapp.di.component.AppComponent;
import com.example.android.coffeeshopapp.di.component.DaggerPresentersComponent;
import com.example.android.coffeeshopapp.di.module.PresentersModule;
import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;
import com.example.android.coffeeshopapp.presenters.RefundPresenter;
import com.example.android.coffeeshopapp.presenters.TransactionListPresenter;
import com.example.android.coffeeshopapp.utils.InternetConnectivityUtil;
import com.example.android.coffeeshopapp.views.RefundView;
import com.example.android.coffeeshopapp.views.TransactionListView;
import com.example.android.coffeeshopapp.widgets.adapters.TransactionListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public class TransactionsActivity extends AppCompatActivity
        implements TransactionListView, TransactionListAdapter.OnRefundClickListener, RefundView {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    TransactionListPresenter transactionListPresenter;
    @Inject
    RefundPresenter refundPresenter;

    private TransactionListAdapter adapter;

    private String filters;
    private long userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        ButterKnife.bind(this);

        DaggerPresentersComponent.builder()
                .appComponent(getAppComponent())
                .presentersModule(new PresentersModule())
                .build()
                .inject(this);

        refundPresenter.setView(this);
        transactionListPresenter.setView(this);

        filters = getIntent().getStringExtra(Constants.PURCHASE_LIST);
        userId = getIntent().getLongExtra(Constants.CARD_ID, 0);

        adapter = new TransactionListAdapter(getContext(), null, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadTransactionList();
    }

    private void loadTransactionList() {
        if (filters.equals(Constants.ALL_USERS)) {
            transactionListPresenter.getAllPurchasesForDay();
        } else {
            transactionListPresenter.getAllPurchases(userId);
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showTransactionList(List<PurchaseTransactionEntity> list) {
        Collections.reverse(list);
        adapter.setTransactionList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefundSuccess(String message) {
        showText(message);
        loadTransactionList();
    }

    @Override
    public void onRefundFailed(String message) {
        showText(message);
    }

    @Override
    public void onRefundClicked(long cardId, long purchaseId) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(getContext());
        edittext.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edittext.setTextColor(getResources().getColor(R.color.colorDarkGrey));
        alert.setMessage("Enter refund amount for " + cardId);
        alert.setTitle(getResources().getString(R.string.refund_title));

        alert.setView(edittext);

        alert.setPositiveButton(getResources().getString(R.string.refund_confirm), (dialog, whichButton) -> {
            if (!InternetConnectivityUtil.isConnected(getContext())) {
                onRefundFailed(getResources().getString(R.string.network_problems));
                return;
            }
            String youEditTextValue = edittext.getText().toString();
            if (youEditTextValue.contains(",")) {
                onRefundFailed(getResources().getString(R.string.use_dot));
                return;
            }
            try {
                double amount = Double.parseDouble(youEditTextValue);
                refundPresenter.refundTransaction(cardId, purchaseId, amount);
            } catch (RuntimeException e) {
                onRefundFailed(getResources().getString(R.string.incorrect_value));
            }
        });

        alert.setNegativeButton(getResources().getString(R.string.cancel), (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });

        alert.show();
    }

    private void showText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public AppComponent getAppComponent() {
        return ((AppCoffeeShop) getApplication()).appComponent();
    }
}
