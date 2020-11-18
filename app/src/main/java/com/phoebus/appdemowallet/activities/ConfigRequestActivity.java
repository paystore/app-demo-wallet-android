package com.phoebus.appdemowallet.activities;

import android.os.Bundle;
import android.os.Handler;
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
import com.phoebus.appdemowallet.models.ResponseCredentials;
import com.phoebus.appdemowallet.services.CredentialService;
import com.phoebus.appdemowallet.utils.ConstantsApp;
import com.phoebus.appdemowallet.utils.DialogUtils;
import com.phoebus.appdemowallet.utils.ImageDialogs;
import com.phoebus.libwallet.service.WalletService;
import com.phoebus.libwallet.utils.Constants;
import com.phoebus.libwallet.utils.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ConfigRequestActivity extends AppCompatActivity {

    private String baseUrl;
    private String token;
    private String notificationToken;

    private EditText edtBaseUrl;
    private EditText edtToken;
    private EditText edtNotificationToken;
    private EditText edtClientId;
    private EditText edtClientSecret;

    private Button btnSubmit;
    private Button btnGetTokenFirebase;
    private Button btnGetTokenApiWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.config);

        setContentView(R.layout.activity_config_request);

        edtBaseUrl = findViewById(R.id.base_url);
        edtToken = findViewById(R.id.token);
        edtClientId = findViewById(R.id.client_id);
        edtClientSecret = findViewById(R.id.client_secret);
        edtNotificationToken = findViewById(R.id.notification_token);

        btnSubmit = findViewById(R.id.btnConfigRequestSubmit);
        btnSubmit.setOnClickListener(v -> submitConfigRequest(v));

        btnGetTokenFirebase = findViewById(R.id.btnGetTokenFirebase);
        btnGetTokenFirebase.setOnClickListener(v -> getTokenFirebase(v));

        btnGetTokenApiWallet = findViewById(R.id.btnGetTokenApiWallet);
        btnGetTokenApiWallet.setOnClickListener(v -> getTokenApiWallet(v));

        setDefaultValues();

    }

    private void setDefaultValues() {
        String baseUrlPref = Helper.readPrefsString(this, ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG);
        this.edtBaseUrl.setText(baseUrlPref == "" ? "http://177.43.232.243:27362/wallet/" : baseUrlPref);
        this.edtClientId.setText("wallet-app-10010");
        this.edtClientSecret.setText("853f91da-4c91-42ac-bc09-f80a4d6177b5");
        this.edtToken.setText(Helper.readPrefsString(this, ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG));
        this.edtNotificationToken.setText(Helper.readPrefsString(this, ConstantsApp.NOTIFICATION_TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG));
    }

    public void submitConfigRequest(View view) {
        this.baseUrl = this.edtBaseUrl.getText().toString();
        this.token = this.edtToken.getText().toString();
        this.notificationToken = this.edtNotificationToken.getText().toString();

        Helper.writePrefs(this, ConstantsApp.BASE_URL_CONFIG, baseUrl, ConstantsApp.PREFS_CONFIG);
        Helper.writePrefs(this, ConstantsApp.TOKEN_CONFIG, "Bearer " + token, ConstantsApp.PREFS_CONFIG);
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
                            Log.w(Constants.TAG, "Error ao obter token do firebase", task.getException());
                            return;
                        }

                        String token = task.getResult().getToken();

                        Log.d(Constants.TAG, "Firebase | ID: " + task.getResult().getId());
                        Log.d(Constants.TAG, "Firebase | Token:" + token);

                        edtNotificationToken.setText(token);
                    }
                });
    }

    private void getTokenApiWallet(View view) {
        String clientId = this.edtClientId.getText().toString();
        String clientSecret = this.edtClientSecret.getText().toString();
        String grantType = "client_credentials";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CredentialService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CredentialService service = retrofit.create(CredentialService.class);
        Call<ResponseCredentials> requestToken = service.token(clientId, clientSecret, grantType);

        Handler handler = new Handler();

        try {
            requestToken.enqueue(new Callback<ResponseCredentials>() {
                @Override
                public void onResponse(Call<ResponseCredentials> call, Response<ResponseCredentials> response) {
                    if(response.isSuccessful()){
                        ResponseCredentials credentials = response.body();
                        Log.i("SUCCESS","code: "+response.code());
                        Log.i("SUCCESS", credentials.toString());
                        edtToken.setText(credentials.getAccessToken());
                    }else{
                        Log.i("ERROR","code: "+response.code());
                        handler.post(() -> DialogUtils.showMessage("Erro - status code: "+response.code(),
                                response.message(),
                                ConfigRequestActivity.this,
                                ImageDialogs.ERROR.ordinal()));
                    }
                }

                @Override
                public void onFailure(Call<ResponseCredentials> call, Throwable t) {
                    Log.e("Failure","Falha na requisição");
                    handler.post(() -> DialogUtils.showMessage(getString(R.string.title_error),
                            t.getMessage(),
                            ConfigRequestActivity.this,
                            ImageDialogs.ERROR.ordinal()));
                }
            });
        }catch (Exception e) {
            Log.e("Error","Falha na requisição");
            handler.post(() -> DialogUtils.showMessage(getString(R.string.title_error),
                    e.getMessage(),
                    ConfigRequestActivity.this,
                    ImageDialogs.ERROR.ordinal()));
        }
    }
}
