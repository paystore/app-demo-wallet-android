package com.phoebus.appdemowallet.activities.cards;

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


public class DeactivateCardActivity extends AppCompatActivity {
    private String cardId;

    private EditText edtCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.deactivateCard);

        setContentView(R.layout.activity_deactivate_card);
        this.edtCardId = this.findViewById(R.id.card_id);
        String lastCardId = Helper.readPrefsString(getApplicationContext(), ConstantsApp.CARD_ID, ConstantsApp.PREFS_CONFIG);
        this.edtCardId.setText(lastCardId);

    }

    public void submitDeactivateCard(View view){
        this.cardId = this.edtCardId.getText().toString();
        Log.d("FORM DeactivateCard", this.cardId);

        deactivateCard();
    }

    private void deactivateCard() {
        Log.d(Constants.TAG, "[IN] deactivateCard()");

        String BASE_URL_WALLET = Helper.readPrefsString(getApplicationContext(), ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG);
        String AUTHORIZATION_TOKEN = Helper.readPrefsString(getApplicationContext(), ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG);

        IWalletApiService apiService = new WalletApiService
                .WalletApiBuilder(getApplicationContext())
                .baseUrlWallet(BASE_URL_WALLET)
                .authorization(AUTHORIZATION_TOKEN)
                .build();

        Handler handler = new Handler();

        try {
            apiService.deactivateCard(cardId, new IWalletCallback<NoContentResponse>() {
                @Override
                public void onResponse(Response<NoContentResponse> response) {

                    if (response.isSuccessful()) {

                        Log.d(Constants.TAG, String.format("statusCode: %s", response.code()));
                        showResult("StatusCode: " + response.code());

                    } else {
                        GeneralErrorResponse generalErrorResponse = ErrorUtils.parseError(response);

                        if (generalErrorResponse != null) {
                            Log.d(Constants.TAG, generalErrorResponse.getTimestamp());
                            Log.d(Constants.TAG, generalErrorResponse.getMessage());
                            Log.d(Constants.TAG, generalErrorResponse.getStatus().toString());
                            Log.d(Constants.TAG, generalErrorResponse.getError());
                            if (generalErrorResponse.getErrors() != null) {
                                for (FieldValidationErrorResponse errorCurrent : generalErrorResponse.getErrors()) {
                                    Log.d(Constants.TAG, errorCurrent.getDefaultMessage());
                                    Log.d(Constants.TAG, errorCurrent.getField());
                                    Log.d(Constants.TAG, errorCurrent.getRejectValue() + "");
                                }

                            }
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
                            DeactivateCardActivity.this,
                            ImageDialogs.ERROR.ordinal()));

                }

            });
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage(), e);

            handler.post(() -> DialogUtils.showMessage(getString(R.string.title_error),
                    e.getMessage(),
                    DeactivateCardActivity.this,
                    ImageDialogs.ERROR.ordinal()));

            Toast.makeText(getApplicationContext(), R.string.infoLogcat, Toast.LENGTH_SHORT).show();
        }
        Log.d(Constants.TAG, "[OUT] deactivateCard()");

    }

    private void showResult(String response) {
        Intent itResponse = new Intent(getApplicationContext(), ResponseActivity.class);
        itResponse.putExtra("result", response);
        startActivity(itResponse);
    }

}
