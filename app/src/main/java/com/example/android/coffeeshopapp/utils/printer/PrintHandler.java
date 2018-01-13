package com.example.android.coffeeshopapp.utils.printer;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.android.coffeeshopapp.R;

import java.io.IOException;

/**
 * Created by dev_serhii on 08.01.2018.
 */

public class PrintHandler {

    public static void printText(Context context, String text, CallbackToClose callbackToClose, boolean close) {
        final boolean isBluetoothConnected = BluetoothUtil.connectBlueTooth(context);
        if (isBluetoothConnected) Toast.makeText(context, context.getString(R.string.bluetooth_connected), Toast.LENGTH_SHORT).show();
        AidlUtil.getInstance().connectPrinterService(context);
        AidlUtil.getInstance().initPrinter();
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getString(R.string.choose_print_method));

        alert.setNegativeButton("AIDL",
                (dialog, whichButton) -> {
                    AidlUtil.getInstance().printText(text, 24, false, false);
                    if (close) callbackToClose.finishActivity();
                });

        alert.setNeutralButton("Bluetooth", (dialogInterface, i) -> {
            if (isBluetoothConnected || BluetoothUtil.connectBlueTooth(context)) {
                printByBluTooth(text);
            }
            if (close) callbackToClose.finishActivity();
        });

        alert.setPositiveButton(context.getResources().getString(R.string.close_receipt),
                (dialog, whichButton) -> {
                    // what ever you want to do with No option.
                });

        alert.show();
    }

    private static void printByBluTooth(String content) {
        try {

            BluetoothUtil.sendData(ESCUtil.boldOff());

            BluetoothUtil.sendData(ESCUtil.underlineOff());

            BluetoothUtil.sendData(ESCUtil.singleByteOff());
            BluetoothUtil.sendData(ESCUtil.setCodeSystem(codeParse(17)));

            BluetoothUtil.sendData(content.getBytes("GB18030"));
            BluetoothUtil.sendData(ESCUtil.nextLine(3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte codeParse(int value) {
        byte res = 0x00;
        switch (value) {
            case 0:
                res = 0x00;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                res = (byte) (value + 1);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                res = (byte) (value + 8);
                break;
            case 12:
                res = 21;
                break;
            case 13:
                res = 33;
                break;
            case 14:
                res = 34;
                break;
            case 15:
                res = 36;
                break;
            case 16:
                res = 37;
                break;
            case 17:
            case 18:
            case 19:
                res = (byte) (value - 17);
                break;
            case 20:
                res = (byte) 0xff;
                break;
        }
        return (byte) res;
    }

    public interface CallbackToClose {
        public boolean close = true;
        void finishActivity();
    }
}
