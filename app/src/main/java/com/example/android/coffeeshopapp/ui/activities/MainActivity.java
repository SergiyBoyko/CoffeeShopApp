package com.example.android.coffeeshopapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.azimolabs.keyboardwatcher.KeyboardWatcher;
import com.example.android.coffeeshopapp.AppCoffeeShop;
import com.example.android.coffeeshopapp.R;
import com.example.android.coffeeshopapp.common.Constants;
import com.example.android.coffeeshopapp.di.component.AppComponent;
import com.example.android.coffeeshopapp.di.component.DaggerPresentersComponent;
import com.example.android.coffeeshopapp.di.module.PresentersModule;
import com.example.android.coffeeshopapp.model.entities.ResponseEntity;
import com.example.android.coffeeshopapp.presenters.UserInfoPresenter;
import com.example.android.coffeeshopapp.utils.InternetConnectivityUtil;
import com.example.android.coffeeshopapp.views.UserInfoView;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements KeyboardWatcher.OnKeyboardToggleListener, UserInfoView {

    // Find out our editable field.
    @BindView(R.id.card_id)
    EditText editText;
    // Find the button which will start editing process.
    @BindView(R.id.button_show_ime)
    ImageButton buttonShowIme;

    @BindView(R.id.but_enter)
    Button enterButton;

    @BindView(R.id.view_all_trans_but)
    Button viewAllTransButton;

    @Inject
    UserInfoPresenter presenter;

    private KeyListener originalKeyListener;
    private KeyboardWatcher keyboardWatcher;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        DaggerPresentersComponent.builder()
                .appComponent(getAppComponent())
                .presentersModule(new PresentersModule())
                .build()
                .inject(this);

        presenter.setView(this);

        // Save its key listener which makes it editable.
        originalKeyListener = editText.getKeyListener();
        // Set it to null - this will make the field non-editable
        editText.setKeyListener(null);

        // Attach an on-click listener.
        buttonShowIme.setOnClickListener(v -> {
            if (editText.getKeyListener() != null)
                lockKeyboard();
            else unlockKeyboard();
        });

        // Set MainActivity as listener for keyboard
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);

        enterButton.setOnClickListener(v -> login());
        viewAllTransButton.setOnClickListener(view -> startTransactionActivity());

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.verifying_progress));
    }

    @Override
    protected void onDestroy() {
        keyboardWatcher.destroy();
        super.onDestroy();
    }

    @Override
    public void onKeyboardShown(int keyboardSize) {
    }

    @Override
    public void onKeyboardClosed() {
        lockKeyboard();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onVerifySuccess(ResponseEntity responseEntity) {
        enterButton.setEnabled(true);
        progressDialog.hide();
        startRoomActivity(responseEntity.getBadgeNumber(),
                responseEntity.getBalance(),
//                responseEntity.getSuffix() + " " +
                responseEntity.getFirstName() + " " +
                        responseEntity.getMiddleName().charAt(0) + " " +
                        responseEntity.getLastName());
    }

    @Override
    public void onVerifyFailed(String message) {
        showText(message);
        enterButton.setEnabled(true);
        progressDialog.hide();
    }

    private void startRoomActivity(String cardId, Double balance, String fullName) {
        Intent intent = new Intent(this, RoomActivity.class);

        intent.putExtra(Constants.FULL_NAME_USER, fullName);
        intent.putExtra(Constants.CARD_ID, cardId);
        intent.putExtra(Constants.BALANCE, balance);

        startActivity(intent);
    }

    private void startTransactionActivity() {
        Intent intent = new Intent(this, TransactionsActivity.class);
        intent.putExtra(Constants.PURCHASE_LIST, Constants.ALL_USERS);
        startActivity(intent);
    }


    private void login() {

        if (editText.length() == 0) {
            onVerifyFailed(getResources().getString(R.string.incorrect_credentials));
            return;
        } else if (!InternetConnectivityUtil.isConnected(this)) {
            onVerifyFailed(getResources().getString(R.string.network_problems));
            return;
        }

        enterButton.setEnabled(false);

        progressDialog.show();

        presenter.getUserData(Integer.parseInt(editText.getText().toString()));
    }

    private void unlockKeyboard() {
        // Restore key listener - this will make the field editable again.
        editText.setKeyListener(originalKeyListener);
        // Focus the field.
        editText.requestFocus();
        // Show soft keyboard for the user to enter the value.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void lockKeyboard() {
        // Hide soft keyboard.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        // Make it non-editable again.
        editText.setKeyListener(null);
    }

    public AppComponent getAppComponent() {
        return ((AppCoffeeShop) getApplication()).appComponent();
    }

    private void showText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
