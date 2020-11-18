package com.phoebus.appdemowallet.activities.cardholders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.phoebus.appdemowallet.R;
import com.phoebus.appdemowallet.activities.ResponseActivity;
import com.phoebus.appdemowallet.utils.ConstantsApp;
import com.phoebus.appdemowallet.utils.DialogUtils;
import com.phoebus.appdemowallet.utils.ImageDialogs;
import com.phoebus.libwallet.models.ConfirmCardholderRequest;
import com.phoebus.libwallet.models.FieldValidationErrorResponse;
import com.phoebus.libwallet.models.GeneralErrorResponse;
import com.phoebus.libwallet.models.IWalletApiService;
import com.phoebus.libwallet.models.IWalletCallback;
import com.phoebus.libwallet.models.NoContentResponse;
import com.phoebus.libwallet.service.WalletApiService;
import com.phoebus.libwallet.utils.Constants;
import com.phoebus.libwallet.utils.ErrorUtils;
import com.phoebus.libwallet.utils.Helper;

import retrofit2.Response;


public class ConfirmCardHolderActivity extends AppCompatActivity {
    private String cardHolderId;
    private String activationCode;

    private EditText edtCardHolderId;
    private EditText edtActivationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.confirmationCardHolder);

        setContentView(R.layout.activity_confirm_card_holder);

        this.edtCardHolderId = this.findViewById(R.id.card_holder_id);
        this.edtActivationCode = this.findViewById(R.id.activation_code);

        setDefaultValues();

    }

    private void setDefaultValues() {
        String cardHolderId = Helper.readPrefsString(getApplicationContext(), ConstantsApp.CARD_HOLDER_ID, ConstantsApp.PREFS_CONFIG);
        this.edtCardHolderId.setText(cardHolderId);
    }

    public void submitConfirmCardHolder(View view) {
        this.cardHolderId = this.edtCardHolderId.getText().toString();
        this.activationCode = this.edtActivationCode.getText().toString();
        Log.d("FORM ConfirmCardHolder", this.cardHolderId + " " + this.activationCode);

        confirmCardholder();

    }

    protected void confirmCardholder() {

        String BASE_URL_WALLET = Helper.readPrefsString(getApplicationContext(), ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG);
        String AUTHORIZATION_TOKEN = Helper.readPrefsString(getApplicationContext(), ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG);

        Log.d(Constants.TAG, "[IN] confirmCardholder()");

        IWalletApiService apiService = new WalletApiService
                .WalletApiBuilder(getApplicationContext())
                .baseUrlWallet(BASE_URL_WALLET)
                .authorization(AUTHORIZATION_TOKEN)
                .build();

        Handler handler = new Handler();

        ConfirmCardholderRequest request = new ConfirmCardholderRequest(this.activationCode);

        try {
            apiService.confirmCardholder(this.cardHolderId, request, new IWalletCallback<NoContentResponse>() {
                @Override
                public void onResponse(Response<NoContentResponse> response) {

                    if (response.isSuccessful()) {

                        Log.d(Constants.TAG, String.format("statusCode: %s", response.code()));
                        showResult("StatusCode: " + response.code());

                    } else {
                        GeneralErrorResponse generalErrorResponse = ErrorUtils.parseError(response);

                        if (generalErrorResponse != null) {
                            Gson gson = new Gson();
                            String jsonInString = gson.toJson(generalErrorResponse);
                            showResult(jsonInString);
                        }

                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.e(Constants.TAG, throwable.getMessage());

                    handler.post(() -> DialogUtils.showMessage(getString(R.string.title_error),
                            throwable.getMessage(),
                            ConfirmCardHolderActivity.this,
                            ImageDialogs.ERROR.ordinal()));
                }

            });
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage(), e);

            handler.post(() -> DialogUtils.showMessage(getString(R.string.title_error),
                    e.getMessage(),
                    ConfirmCardHolderActivity.this,
                    ImageDialogs.ERROR.ordinal()));

            Toast.makeText(getApplicationContext(), R.string.infoLogcat, Toast.LENGTH_SHORT).show();
        }
        Log.d(Constants.TAG, "[OUT] confirmCardholder()");
    }

    private void showResult(String response) {
        Intent itResponse = new Intent(getApplicationContext(), ResponseActivity.class);
        itResponse.putExtra("result", response);
        startActivity(itResponse);
    }

}
