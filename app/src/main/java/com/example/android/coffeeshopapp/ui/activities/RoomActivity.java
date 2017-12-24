package com.example.android.coffeeshopapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.View;
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
import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;
import com.example.android.coffeeshopapp.presenters.TransactionPresenter;
import com.example.android.coffeeshopapp.utils.InternetConnectivityUtil;
import com.example.android.coffeeshopapp.views.TransactionView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public class RoomActivity extends AppCompatActivity implements KeyboardWatcher.OnKeyboardToggleListener, TransactionView,
        View.OnClickListener {

    @BindView(R.id.amount_field_int)
    EditText intPartText;
    @BindView(R.id.amount_field_fractional)
    EditText fractPartText;
    //    @BindView(R.id.button_show_ime)
//    ImageButton buttonShowIme;
    @BindView(R.id.transaction_but)
    Button transactionButton;
    @BindView(R.id.full_client_name)
    TextView fullNameView;
    @BindView(R.id.card_number)
    TextView cardIdView;
    @BindView(R.id.card_balance)
    TextView balanceView;
    @BindView(R.id.view_user_trans_but)
    Button viewUserTransButton;
    @BindView(R.id.upd_button)
    ImageButton updateBalanceTransButton;
    @BindView(R.id.num1)
    Button num1;
    @BindView(R.id.num2)
    Button num2;
    @BindView(R.id.num3)
    Button num3;
    @BindView(R.id.num4)
    Button num4;
    @BindView(R.id.num5)
    Button num5;
    @BindView(R.id.num6)
    Button num6;
    @BindView(R.id.num7)
    Button num7;
    @BindView(R.id.num8)
    Button num8;
    @BindView(R.id.num9)
    Button num9;
    @BindView(R.id.numX)
    Button numX;
    @BindView(R.id.num0)
    Button num0;
    @BindView(R.id.numZ)
    Button numZ;

    @Inject
    TransactionPresenter presenter;

    private KeyListener originalKeyListener1;
    private KeyListener originalKeyListener2;
    private KeyboardWatcher keyboardWatcher;

    private ProgressDialog progressDialog;

    private String cardId;
    private double balance;
    private String fullName;

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

        fullName = getIntent().getStringExtra(Constants.FULL_NAME_USER);
        cardId = getIntent().getStringExtra(Constants.CARD_ID);
        balance = getIntent().getDoubleExtra(Constants.BALANCE, 0);

        fullNameView.setText(fullName);
        cardIdView.setText(cardId);
        balanceView.setText(String.format(Locale.ENGLISH, "%.2f", balance));

        originalKeyListener1 = intPartText.getKeyListener();
        originalKeyListener2 = fractPartText.getKeyListener();

        intPartText.setKeyListener(null);
        fractPartText.setKeyListener(null);

//        buttonShowIme.setOnClickListener(v -> {
//            if (intPartText.getKeyListener() != null && fractPartText.getKeyListener() != null)
//                lockKeyboard();
//            else unlockKeyboard();
//        });

        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);

        transactionButton.setOnClickListener(v -> confirm());

        viewUserTransButton.setOnClickListener(view -> startTransactionActivity());
        updateBalanceTransButton.setOnClickListener(view -> presenter.getBalance(cardId));

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.verifying_progress));

        setupButtonsListener();
    }

    @Override
    public void transactionSuccess(PurchaseTransactionEntity transactionEntity) {
        progressDialog.hide();
//        showText(message);
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH);
        showText(String.format(Locale.ENGLISH, "Transaction Success: %.2f", transactionEntity.getPrice()));
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("CARD ID: " + transactionEntity.getBadgeId()
                + "\nFull Name: " + fullName
                + "\nAmount: " + String.format(Locale.ENGLISH, "%.2f", transactionEntity.getPrice())
                + "\nDate: " + dateFormat.format(new Date(transactionEntity.getDate())));
        alert.setTitle(getResources().getString(R.string.receipt_title));

        alert.setPositiveButton(getResources().getString(R.string.print_receipt),
                (dialog, whichButton) -> showText("Coming soon."));

        alert.setNegativeButton(getResources().getString(R.string.close_receipt), (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });

        alert.show();
        presenter.getBalance(cardId);
    }

    @Override
    public void onClick(View view) {
        String tag = view.getTag().toString();
        switch (tag) {
            case "AC":
                intPartText.setText("");
                fractPartText.setText("");
                break;
            case ".":
                fractPartText.requestFocus();
                break;
            default:
                if (intPartText.hasFocus()) {
                    String intAmount = intPartText.getText().toString() + tag;
                    intPartText.setText(intAmount);
                } else {
                    String fractAmount = fractPartText.getText().toString();
                    if (fractAmount.length() < 2) fractAmount += tag;
                    fractPartText.setText(fractAmount);
                }
        }
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
        transactionButton.setEnabled(true);
        progressDialog.hide();
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
        transactionButton.setEnabled(false);
        progressDialog.show();

        if (!InternetConnectivityUtil.isConnected(this)) {
            transactionFailed(getResources().getString(R.string.network_problems));
            return;
        }

        String intPart = intPartText.getText().length() == 0 ? "0" : intPartText.getText().toString();
        String fractPart = fractPartText.getText().length() == 0 ? "0" : fractPartText.getText().toString();
        String amount = intPart + "." + fractPart;
        Double price = Double.parseDouble(amount);

        if ((intPartText.length() != 0 || fractPartText.length() != 0) && price > 0) {
            presenter.confirmTransaction(cardId, Double.parseDouble(amount));
        } else {
            transactionFailed(getString(R.string.too_low_amount));
        }
    }

    private void startTransactionActivity() {
        Intent intent = new Intent(this, TransactionsActivity.class);
        intent.putExtra(Constants.PURCHASE_LIST, Constants.CONCRETE_USER);
        intent.putExtra(Constants.CARD_ID, cardId);
        startActivity(intent);
    }

    private void setupButtonsListener() {
        findViewById(R.id.num1).setOnClickListener(this);
        findViewById(R.id.num2).setOnClickListener(this);
        findViewById(R.id.num3).setOnClickListener(this);
        findViewById(R.id.num4).setOnClickListener(this);
        findViewById(R.id.num5).setOnClickListener(this);
        findViewById(R.id.num6).setOnClickListener(this);
        findViewById(R.id.num7).setOnClickListener(this);
        findViewById(R.id.num8).setOnClickListener(this);
        findViewById(R.id.num9).setOnClickListener(this);
        findViewById(R.id.numX).setOnClickListener(this);
        findViewById(R.id.num0).setOnClickListener(this);
        findViewById(R.id.numZ).setOnClickListener(this);
    }
}
