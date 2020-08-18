package com.phoebus.appdemowallet.security;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.phoebus.libwallet.R;
import com.phoebus.libwallet.utils.Constants;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Classe Responsável por solicitar a permissão ao usuário da utilização dos métodos da lib.
 * A estrategia é solicita as permissões de uma vez só.
 */

public class WalletPermissions {

    private static final int RC_ALL_PERMISSION = 0;

    private Activity activity;
    private Context context;
    private static WalletPermissions instance;

    private WalletPermissions(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public static WalletPermissions getInstance(Activity activity) {

        if (instance == null) {

            instance = new WalletPermissions(activity);
        }

        return instance;
    }

    @AfterPermissionGranted(RC_ALL_PERMISSION)
    public void requiresPermission() {
        String[] perms = {Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CAMERA
        };
        if (EasyPermissions.hasPermissions(context, perms)) {
            // Already have permission, do the thing
            // ...
            Log.d(Constants.TAG, "Eu já tenho permissao");
        } else {
            // Do not have permissions, request them now
            Log.d(Constants.TAG, "Eu NÃO tenho permissao");
            EasyPermissions.requestPermissions(activity, context.getString(R.string.read_phone_state),
                    RC_ALL_PERMISSION, perms);
        }

    }


}
