package com.phoebus.appdemowallet.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.phoebus.appdemowallet.R;
import com.phoebus.appdemowallet.utils.ConstantsApp;
import com.phoebus.libwallet.service.WalletService;
import com.phoebus.libwallet.utils.Constants;
import com.phoebus.libwallet.utils.Helper;


public class ConfigRequestActivity extends AppCompatActivity {

    private String baseUrl;
    private String token;
    private String notificationToken;

    private EditText edtBaseUrl;
    private EditText edtToken;
    private EditText edtNotificationToken;

    private Button btnSubmit;
    private Button btnGetTokenFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.config);

        setContentView(R.layout.activity_config_request);

        edtBaseUrl = findViewById(R.id.base_url);
        edtToken = findViewById(R.id.token);
        edtNotificationToken = findViewById(R.id.notification_token);

        btnSubmit = findViewById(R.id.btnConfigRequestSubmit);
        btnSubmit.setOnClickListener(v -> submitConfigRequest(v));

        btnGetTokenFirebase = findViewById(R.id.btnGetTokenFirebase);
        btnGetTokenFirebase.setOnClickListener(v -> getTokenFirebase(v));

        setDefaultValues();

    }

    private void setDefaultValues() {
        String baseUrlPref = Helper.readPrefsString(this, ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG);
        this.edtBaseUrl.setText(baseUrlPref == "" ? "http://177.43.232.243:27362/wallet/" : baseUrlPref);
        this.edtToken.setText(Helper.readPrefsString(this, ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG));
        this.edtNotificationToken.setText(Helper.readPrefsString(this, ConstantsApp.NOTIFICATION_TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG));
    }

    public void submitConfigRequest(View view) {
        this.baseUrl = this.edtBaseUrl.getText().toString();
        this.token = this.edtToken.getText().toString();
        this.notificationToken = this.edtNotificationToken.getText().toString();

        Helper.writePrefs(this, ConstantsApp.BASE_URL_CONFIG, baseUrl, ConstantsApp.PREFS_CONFIG);
        Helper.writePrefs(this, ConstantsApp.TOKEN_CONFIG, token, ConstantsApp.PREFS_CONFIG);
        Helper.writePrefs(this, ConstantsApp.NOTIFICATION_TOKEN_CONFIG, notificationToken, ConstantsApp.PREFS_CONFIG);


        WalletService.reloadInstanceHttp(baseUrl); //reload base url
        Toast.makeText(this, this.getString(R.string.config_save), Toast.LENGTH_LONG).show();
        finish();
    }

    private void getTokenFirebase(View view) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(Constants.TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        edtNotificationToken.setText(token);

                        // Log and toast
                        String msg = "token Firebase " + token;
                        Log.d(Constants.TAG, msg);
                    }
                });
    }
}
