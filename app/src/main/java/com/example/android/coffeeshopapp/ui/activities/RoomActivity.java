package com.example.android.coffeeshopapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.azimolabs.keyboardwatcher.KeyboardWatcher;
import com.example.android.coffeeshopapp.AppCoffeeShop;
import com.example.android.coffeeshopapp.R;
import com.example.android.coffeeshopapp.common.Constants;
import com.example.android.coffeeshopapp.di.component.AppComponent;
import com.example.android.coffeeshopapp.di.component.DaggerPresentersComponent;
import com.example.android.coffeeshopapp.di.module.PresentersModule;
import com.example.android.coffeeshopapp.presenters.TransactionPresenter;
import com.example.android.coffeeshopapp.views.TransactionView;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public class RoomActivity extends AppCompatActivity implements KeyboardWatcher.OnKeyboardToggleListener, TransactionView {

    @BindView(R.id.amount_field_int)
    EditText intPartText;
    @BindView(R.id.amount_field_fractional)
    EditText fractPartText;
    @BindView(R.id.button_show_ime)
    ImageButton buttonShowIme;
    @BindView(R.id.transaction_but)
    Button transactionButton;
    @BindView(R.id.card_number)
    TextView cardIdView;
    @BindView(R.id.card_balance)
    TextView balanceView;

    @Inject
    TransactionPresenter presenter;

    private KeyListener originalKeyListener1;
    private KeyListener originalKeyListener2;
    private KeyboardWatcher keyboardWatcher;

    private ProgressDialog progressDialog;

    private long cardId;
    private double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        ButterKnife.bind(this);
        DaggerPresentersComponent.builder()
                .appComponent(getAppComponent())
                .presentersModule(new PresentersModule())
                .build()
                .inject(this);

        presenter.setView(this);

        cardId = getIntent().getLongExtra(Constants.CARD_ID, 0);
        balance = getIntent().getDoubleExtra(Constants.BALANCE, 0);

        cardIdView.setText(String.valueOf(cardId));
        balanceView.setText(String.format(Locale.ENGLISH, "%.2f", balance));

        originalKeyListener1 = intPartText.getKeyListener();
        originalKeyListener2 = fractPartText.getKeyListener();

        intPartText.setKeyListener(null);
        fractPartText.setKeyListener(null);

        buttonShowIme.setOnClickListener(v -> {
            if (intPartText.getKeyListener() != null && fractPartText.getKeyListener() != null)
                lockKeyboard();
            else unlockKeyboard();
        });

        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);

        transactionButton.setOnClickListener(v -> {
            confirm();

            // hide keyboard if its possible
//            if (getCurrentFocus() != null) {
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                lockKeyboard();
//            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Verifying...");
    }

    @Override
    public void transactionSuccess(String message) {
        transactionButton.setEnabled(true);
        progressDialog.hide();
        showText(message);
        presenter.getBalance(cardId);
    }

    @Override
    public void transactionFailed(String message) {
        transactionButton.setEnabled(true);
        progressDialog.hide();
        showText(message);
    }

    @Override
    public void reloadBalance(double balance) {
        balanceView.setText(String.format(Locale.ENGLISH, "%.2f", balance));
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onKeyboardShown(int keyboardSize) {
    }

    @Override
    public void onKeyboardClosed() {
        lockKeyboard();
    }

    private void unlockKeyboard() {
        intPartText.setKeyListener(originalKeyListener1);
        fractPartText.setKeyListener(originalKeyListener2);
        intPartText.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(intPartText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void lockKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(intPartText.getWindowToken(), 0);

        intPartText.setKeyListener(null);
        fractPartText.setKeyListener(null);
    }

    public AppComponent getAppComponent() {
        return ((AppCoffeeShop) getApplication()).appComponent();
    }

    private void showText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    private void confirm() {
        String intPart = intPartText.getText().length() == 0 ? "0" : intPartText.getText().toString();
        String fractPart = fractPartText.getText().length() == 0 ? "0" : fractPartText.getText().toString();
        String amount = intPart + "." + fractPart;
        Double price = Double.parseDouble(amount);

        if ((intPartText.length() != 0 || fractPartText.length() != 0) && price > 0) {
            presenter.confirmTransaction(cardId, Double.parseDouble(amount));
        } else {
            transactionButton.setEnabled(false);
            progressDialog.show();
            transactionFailed(getString(R.string.too_low_amount));
        }
    }
}
