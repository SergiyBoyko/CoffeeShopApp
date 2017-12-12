package com.example.android.coffeeshopapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.azimolabs.keyboardwatcher.KeyboardWatcher;
import com.example.android.coffeeshopapp.R;
import com.example.android.coffeeshopapp.utils.InternetConnectivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements KeyboardWatcher.OnKeyboardToggleListener {

    // Find out our editable field.
    @BindView(R.id.card_id)
    EditText editText;
    // Find the button which will start editing process.
    @BindView(R.id.button_show_ime)
    ImageButton buttonShowIme;

    @BindView(R.id.but_enter)
    Button enterButton;

    private KeyListener originalKeyListener;
    private KeyboardWatcher keyboardWatcher;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Save its key listener which makes it editable.
        originalKeyListener = editText.getKeyListener();
        // Set it to null - this will make the field non-editable
        editText.setKeyListener(null);
        buttonShowIme = (ImageButton) findViewById(R.id.button_show_ime);

        // Attach an on-click listener.
        buttonShowIme.setOnClickListener(v -> {
            if (editText.getKeyListener() != null)
                lockKeyboard();
            else unlockKeyboard();
        });

        // Set MainActivity as listener for keyboard
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);

        enterButton.setOnClickListener(v -> {
            login();

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

    private void unlockKeyboard() {
        // Restore key listener - this will make the field editable again.
        editText.setKeyListener(originalKeyListener);
        // Focus the field.
        editText.requestFocus();
        // Show soft keyboard for the user to enter the value.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void login() {

        if (editText.length() == 0) {
//            onLoginFailed(getResources().getString(R.string.incorrect_credentials));
            showText("incorrect_credentials");
            return;
        } else if (!InternetConnectivityUtil.isConnected(this)) {
//            onLoginFailed(getResources().getString(R.string.network_problems));
            showText("network_problems");
            return;
        }

        enterButton.setEnabled(false);

        progressDialog.show();

//        String username = usernameText.getText().toString();
//        String password = passwordText.getText().toString();
//
//        presenter.login(username, password);
    }

    private void lockKeyboard() {
        // Hide soft keyboard.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        // Make it non-editable again.
        editText.setKeyListener(null);
    }

    private void showText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}