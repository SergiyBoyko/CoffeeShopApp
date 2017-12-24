package com.example.android.coffeeshopapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.WindowManager;
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
import com.example.android.coffeeshopapp.model.entities.ResponseEntity;
import com.example.android.coffeeshopapp.presenters.UserInfoPresenter;
import com.example.android.coffeeshopapp.utils.InternetConnectivityUtil;
import com.example.android.coffeeshopapp.views.UserInfoView;

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

    private KeyboardWatcher keyboardWatcher;

    private ProgressDialog progressDialog;

    private int originalInputType;
    private boolean isKeyboardLock;

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
        //865099026032140
        //d9e76c56e5d68c1c  f4 ef e9 94 96 ff
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String uid1 = tManager.getDeviceId();
        String uid2 = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        String s = "1)DeviceId " + uid1 + "\n2)AndroidId " + uid2 + "\n3)mac: " + address;
        ((TextView)findViewById(R.id.textView1)).setText(s);
        showText(s);

        isKeyboardLock = true;
        editText.requestFocus();
        originalInputType = editText.getInputType();

        // Attach an on-click listener.
        buttonShowIme.setOnClickListener(v -> {
            if (!isKeyboardLock)
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
//        showText("keyboard shown");
        if (isKeyboardLock) lockKeyboard();
    }

    @Override
    public void onKeyboardClosed() {
//        showText("keyboard closed");
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

        presenter.getUserData(editText.getText().toString());
    }

    private void unlockKeyboard() {
        isKeyboardLock = false;
        // Focus the field.
        editText.setInputType(originalInputType);
        editText.requestFocus();
        // Show soft keyboard for the user to enter the value.
        showTheKeyboard(getContext(), editText);
    }

    private void lockKeyboard() {
        isKeyboardLock = true;
        // Hide soft keyboard.
        hideTheKeyboard(getContext(), editText);
        // Make it non-editable again.
//        hideTheKeyboardSecond(editText);
    }

    public AppComponent getAppComponent() {
        return ((AppCoffeeShop) getApplication()).appComponent();
    }

    private void showText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method for showing the Keyboard
     * @param context The context of the activity
     * @param editText The edit text for which we want to show the keyboard
     */
    public void showTheKeyboard(Context context, EditText editText){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Method for hiding the Keyboard
     * @param context The context of the activity
     * @param editText The edit text for which we want to hide the keyboard
     */
    public void hideTheKeyboard(Context context, EditText editText){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
    /**
     * Another method to hide the keyboard if the above method is not working.
     */
    public void hideTheKeyboardSecond(EditText editText){
        editText.setInputType(InputType.TYPE_NULL);
    }
}
