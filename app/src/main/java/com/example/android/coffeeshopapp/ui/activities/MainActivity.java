package com.example.android.coffeeshopapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
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
import com.example.android.coffeeshopapp.model.entities.ResponseEntity;
import com.example.android.coffeeshopapp.presenters.UserInfoPresenter;
import com.example.android.coffeeshopapp.presenters.ZXReportPresenter;
import com.example.android.coffeeshopapp.utils.InternetConnectivityUtil;
import com.example.android.coffeeshopapp.utils.printer.PrintHandler;
import com.example.android.coffeeshopapp.views.UserInfoView;
import com.example.android.coffeeshopapp.views.ZXReportView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements KeyboardWatcher.OnKeyboardToggleListener,
        UserInfoView, ZXReportView, PrintHandler.CallbackToClose {

    // Find out our editable field.
    @BindView(R.id.card_id)
    EditText editText;
    // Find the button which will start editing process.
    @BindView(R.id.button_show_ime)
    ImageButton buttonShowIme;

    @BindView(R.id.but_enter)
    Button enterButton;

    @BindView(R.id.x_report_but)
    Button xButton;
    @BindView(R.id.z_report_but)
    Button zButton;

    @BindView(R.id.view_all_trans_but)
    Button viewAllTransButton;

    @Inject
    UserInfoPresenter userInfoPresenter;

    @Inject
    ZXReportPresenter reportPresenter;

    private KeyboardWatcher keyboardWatcher;

    private ProgressDialog progressDialog;

    private int originalInputType;
    private boolean isKeyboardLock;

    String uniqueId;

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

        userInfoPresenter.setView(this);
        reportPresenter.setView(this);
        uniqueId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String mainMessage = getString(R.string.terminal_id) + uniqueId;
        ((TextView) findViewById(R.id.textView1)).setText(mainMessage);

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

        setXZReportButtonsListeners();
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

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showZReport(List<PurchaseTransactionEntity> transactionEntities, long lastTimeUpdate) {
        progressDialog.hide();
        showReport(transactionEntities, lastTimeUpdate, Constants.Z_REPORT);
    }

    @Override
    public void showXReport(List<PurchaseTransactionEntity> transactionEntities, long lastTimeUpdate) {
        progressDialog.hide();
        showReport(transactionEntities, lastTimeUpdate, Constants.X_REPORT);
    }

    private void showReport(List<PurchaseTransactionEntity> transactionEntities, long lastTimeUpdate, String title) {
        if (lastTimeUpdate == 0 && transactionEntities.size() > 0) {
            lastTimeUpdate = transactionEntities.get(0).getDate();
        }
        Date date = new Date(lastTimeUpdate);
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH);
        double totalAmount = 0;
        StringBuilder receiptMessage = new StringBuilder(title + "\nUnique device id: " + uniqueId +
                "\nLast time update: " + dateFormat.format(date));
        for (PurchaseTransactionEntity entity : transactionEntities) {
            receiptMessage
                    .append("\nDate: ").append(dateFormat.format(new Date(entity.getDate())))
                    .append(" ID: ").append(entity.getBadgeId())
                    .append(" Amount: ").append(String.format(Locale.ENGLISH, "%.2f", entity.getPrice()));
            totalAmount += entity.getPrice();
        }
        receiptMessage.append("\nTotal: ").append(String.format(Locale.ENGLISH, "%.2f", totalAmount));

        View promptView = LayoutInflater.from(this).inflate(R.layout.zx_report_dialog, null);
        TextView messageTextView = (TextView) promptView.findViewById(R.id.textmsg);
        messageTextView.setText(receiptMessage);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(promptView);
        alert.setTitle(title);

        alert.setPositiveButton(getResources().getString(R.string.print_receipt),
                (dialog, whichButton) -> {
                    final String message = receiptMessage.toString();
                    PrintHandler.printText(MainActivity.this, message, null, false);
                });

        alert.setNegativeButton(getResources().getString(R.string.close_receipt), (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });
        alert.show();
    }

    private void setXZReportButtonsListeners() {
        xButton.setOnClickListener(view -> {
            if (InternetConnectivityUtil.isConnected(this)) {
                progressDialog.show();
                reportPresenter.getXReportWithLastTimeUpdate(uniqueId);
            } else showText(getString(R.string.network_problems));
        });

        zButton.setOnClickListener(view -> {
            if (InternetConnectivityUtil.isConnected(this)) {
                progressDialog.show();
                reportPresenter.getZReportWithLastTimeUpdate(uniqueId);
            } else showText(getString(R.string.network_problems));
        });
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

        userInfoPresenter.getUserData(editText.getText().toString());
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
     *
     * @param context  The context of the activity
     * @param editText The edit text for which we want to show the keyboard
     */
    public void showTheKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Method for hiding the Keyboard
     *
     * @param context  The context of the activity
     * @param editText The edit text for which we want to hide the keyboard
     */
    public void hideTheKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * Another method to hide the keyboard if the above method is not working.
     */
    public void hideTheKeyboardSecond(EditText editText) {
        editText.setInputType(InputType.TYPE_NULL);
    }
}
