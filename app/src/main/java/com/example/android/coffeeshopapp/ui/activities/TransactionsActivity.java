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

        String filters = getIntent().getStringExtra(Constants.PURCHASE_LIST);
        List<PurchaseTransactionEntity> list = null;
        if (filters.equals(Constants.ALL_USERS)) {
//            transactionListPresenter.getAllPurchasesForDay();
            list = loadTestData();
        } else {
            long id = getIntent().getLongExtra(Constants.CARD_ID, 0);
//            transactionListPresenter.getAllPurchases(id);
            list = new ArrayList<>();
            PurchaseTransactionEntity p1 = new PurchaseTransactionEntity();
            p1.setCardId(id);
            p1.setDate(1513036800000L);
            p1.setId(100L);
            p1.setPrice(1000.50);
            list.add(p1);
        }

        adapter = new TransactionListAdapter(getContext(), list, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showTransactionList(List<PurchaseTransactionEntity> list) {
        adapter.setTransactionList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefundSuccess(String message) {
        showText(message);
    }

    @Override
    public void onRefundFailed(String message) {
        showText(message);
    }

    @Override
    public void onRefundClicked(long cardId, long purchaseId) {
//        showText("refund " + purchaseId);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(getContext());
        edittext.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edittext.setTextColor(getResources().getColor(R.color.colorDarkGrey));
        alert.setMessage("Enter refund amount for " + cardId);
        alert.setTitle("Refund");

        alert.setView(edittext);

        alert.setPositiveButton("Confirm Refund", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!InternetConnectivityUtil.isConnected(getContext())) {
                    onRefundFailed(getResources().getString(R.string.network_problems));
                    return;
                }
                String youEditTextValue = edittext.getText().toString();
                showText(youEditTextValue);
                try {
                    double amount = Double.parseDouble(youEditTextValue);
//                    refundPresenter.refundTransaction(cardId, purchaseId, amount);
                    showText("sum " + amount);
                } catch (RuntimeException e) {
                    onRefundFailed("Incorrect value");
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    private void showText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public AppComponent getAppComponent() {
        return ((AppCoffeeShop) getApplication()).appComponent();
    }

    private List<PurchaseTransactionEntity> loadTestData() {
        List<PurchaseTransactionEntity> list = new ArrayList<>();
        PurchaseTransactionEntity p1 = new PurchaseTransactionEntity();
        p1.setCardId(12345L);
        p1.setDate(1513036800000L);
        p1.setId(1L);
        p1.setPrice(101.50);
        list.add(p1);
        PurchaseTransactionEntity p2 = new PurchaseTransactionEntity();
        p2.setCardId(22345L);
        p2.setDate(1513036800000L);
        p2.setId(2L);
        p2.setPrice(102.55);
        list.add(p2);
        PurchaseTransactionEntity p3 = new PurchaseTransactionEntity();
        p3.setCardId(32345L);
        p3.setDate(1513036800000L);
        p3.setId(3L);
        p3.setPrice(200D);
        list.add(p3);
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p1);
        list.add(p2);
        list.add(p3);
        return list;
    }
}
