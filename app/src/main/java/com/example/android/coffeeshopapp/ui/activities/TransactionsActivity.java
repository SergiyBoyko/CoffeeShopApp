package com.example.android.coffeeshopapp.ui.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.android.coffeeshopapp.ui.activities.printer.TextActivity;
import com.example.android.coffeeshopapp.utils.printer.BluetoothUtil;
import com.example.android.coffeeshopapp.utils.printer.BytesUtil;
import com.example.android.coffeeshopapp.utils.InternetConnectivityUtil;
import com.example.android.coffeeshopapp.views.RefundView;
import com.example.android.coffeeshopapp.views.TransactionListView;
import com.example.android.coffeeshopapp.widgets.adapters.TransactionListAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private String userId;
    private String uniqueId;

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

        uniqueId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        refundPresenter.setView(this);
        transactionListPresenter.setView(this);

        filters = getIntent().getStringExtra(Constants.PURCHASE_LIST);
        userId = getIntent().getStringExtra(Constants.CARD_ID);

        adapter = new TransactionListAdapter(getContext(), null, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadTransactionList();
    }

    private void loadTransactionList() {
        if (filters.equals(Constants.ALL_USERS)) {
            transactionListPresenter.getAllPurchasesForDay(uniqueId);
        } else {
            transactionListPresenter.getAllPurchases(userId, uniqueId);
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
    public void onRefundSuccess(PurchaseTransactionEntity transactionEntity, String fullName) {
//        showText("onRefundSuccess " + (transactionEntity != null));
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH);
            showText(String.format(Locale.ENGLISH, "Refund Success: %.2f", transactionEntity.getPrice()));
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            String message = "CARD ID: " + transactionEntity.getBadgeId()
                    + "\nFull Name: " + fullName
                    + "\nAmount: -" + String.format(Locale.ENGLISH, "%.2f", transactionEntity.getPrice())
                    + "\nDate: " + dateFormat.format(new Date(transactionEntity.getDate()));
            alert.setMessage(message);
            alert.setTitle(getResources().getString(R.string.receipt_title));

            alert.setPositiveButton(getResources().getString(R.string.print_receipt),
                    (dialog, whichButton) -> {
                        // TODO: 29.12.2017 implement print action
                        Intent intent = new Intent(this, TextActivity.class);
                        intent.putExtra(Constants.PRINT_TEXT_EXTRA, message);
                        startActivity(intent);
                        finish();
                    });

            alert.setNegativeButton(getResources().getString(R.string.close_receipt),
                    (dialog, whichButton) -> {
                        // what ever you want to do with No option.
                        finish();
                    });
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadTransactionList();
    }

    @Override
    public void onRefundFailed(String message) {
        showText(message);
    }

    @Override
    public void onPinVerifySuccess(String message) {
        showText(message);
    }

    @Override
    public void onRefundClicked(String cardId, long purchaseId, double purchaseAmount, String fullName) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Enter refund amount for " + cardId +
                "\nAmount: " + String.format(Locale.ENGLISH, "%.2f", purchaseAmount));
        alert.setTitle(getResources().getString(R.string.refund_title));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText amountEditText = new EditText(getContext());
        amountEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        amountEditText.setTextColor(getResources().getColor(R.color.colorDarkGrey));
        amountEditText.setHint(getResources().getString(R.string.enter_amount_here));
        amountEditText.setHintTextColor(getResources().getColor(R.color.colorDarkGrey));
        layout.addView(amountEditText);

        final EditText pinEditText = new EditText(getContext());
        pinEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        pinEditText.setTextColor(getResources().getColor(R.color.colorDarkGrey));
        pinEditText.setHint("Enter pin here");
        pinEditText.setHintTextColor(getResources().getColor(R.color.colorDarkGrey));
        pinEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        layout.addView(pinEditText);

        alert.setView(layout);

        alert.setPositiveButton(getResources().getString(R.string.refund_confirm), (dialog, whichButton) -> {
            if (!InternetConnectivityUtil.isConnected(getContext())) {
                onRefundFailed(getResources().getString(R.string.network_problems));
                return;
            }
            String youEditTextValue = amountEditText.getText().toString();
            if (youEditTextValue.contains(",")) {
                onRefundFailed(getResources().getString(R.string.use_dot));
                return;
            }
            String pin = pinEditText.getText().toString();
            if (pin.length() != 4) {
                onRefundFailed("PIN must have 4 digits");
                return;
            }
            try {
                double amount = Double.parseDouble(youEditTextValue);
                if (amount != 0)
                    refundPresenter.tryToRefund(pin, cardId, purchaseId, amount, uniqueId, fullName);
//                    refundPresenter.refundTransaction(cardId, purchaseId, amount, uniqueId);
            } catch (NumberFormatException e) {
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
