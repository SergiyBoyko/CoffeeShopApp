package com.example.android.coffeeshopapp.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.azimolabs.keyboardwatcher.KeyboardWatcher;
import com.example.android.coffeeshopapp.AppCoffeeShop;
import com.example.android.coffeeshopapp.R;
import com.example.android.coffeeshopapp.common.Constants;
import com.example.android.coffeeshopapp.di.component.AppComponent;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev_serhii on 12.12.2017.
 */

public class RoomActivity extends AppCompatActivity implements KeyboardWatcher.OnKeyboardToggleListener {

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

    private KeyListener originalKeyListener1;
    private KeyListener originalKeyListener2;
    private KeyboardWatcher keyboardWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        ButterKnife.bind(this);

        Long card_id = getIntent().getLongExtra(Constants.CARD_ID, 0);
        Double balance = getIntent().getDoubleExtra(Constants.BALANCE, 0);

        cardIdView.setText(String.valueOf(card_id));
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

}
