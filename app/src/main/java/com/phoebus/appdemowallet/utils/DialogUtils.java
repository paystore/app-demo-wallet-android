package com.phoebus.appdemowallet.utils;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;

import androidx.appcompat.app.AlertDialog;

import com.phoebus.appdemowallet.R;

public class DialogUtils {


    public static void showMessage(final String title, final String msg,
                                 final Context c) {


        AlertDialog.Builder alerta = new AlertDialog.Builder(c);
        alerta.setTitle(title);
        alerta.setMessage(msg);
        alerta.setNeutralButton("OK", null);
        alerta.create();
        alerta.show();
    }

    public static void showMessage(final String title, final String msg,
                                 final Context c, final OnClickListener listener,
                                 final int imageType) {

        AlertDialog.Builder alerta = new AlertDialog.Builder(c);
        alerta.setTitle(title);
        alerta.setMessage(msg);

        if (imageType == 0) {
            alerta.setIcon(R.drawable.ic_msg_info);
        } else if (imageType == 1) {
            alerta.setIcon(R.drawable.ic_msg_alerta);
        } else if (imageType == 2) {
            alerta.setIcon(R.drawable.ic_msg_erro);
        }

        alerta.setNeutralButton("OK", listener);

        alerta.create();
        alerta.show();

    }

    public static void showMessage(final String title, final String msg,
                                 final Context c, final int imageType) {

        AlertDialog.Builder alerta = new AlertDialog.Builder(c);
        alerta.setTitle(title);
        alerta.setMessage(msg);
        if (imageType == 0) {
            alerta.setIcon(R.drawable.ic_msg_info);
        } else if (imageType == 1) {
            alerta.setIcon(R.drawable.ic_msg_alerta);
        } else if (imageType == 2) {
            alerta.setIcon(R.drawable.ic_msg_erro);
        }

        alerta.setNeutralButton("OK", null);
        alerta.create();
        alerta.show();
    }

}
