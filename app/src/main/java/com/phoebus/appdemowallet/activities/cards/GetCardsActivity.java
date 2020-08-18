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
import com.phoebus.libwallet.models.GetCardResponse;
import com.phoebus.libwallet.models.IWalletApiService;
import com.phoebus.libwallet.models.IWalletCallback;
import com.phoebus.libwallet.service.WalletApiService;
import com.phoebus.libwallet.utils.Constants;
import com.phoebus.libwallet.utils.ErrorUtils;
import com.phoebus.libwallet.utils.Helper;

import java.util.List;

import retrofit2.Response;


public class GetCardsActivity extends AppCompatActivity {
    private String cardholderId;

    private EditText edtCardholderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.getCards);

        setContentView(R.layout.activity_get_cards);
        this.edtCardholderId = this.findViewById(R.id.card_holder_id);
        String lastCardHolderId = Helper.readPrefsString(getApplicationContext(), ConstantsApp.CARD_HOLDER_ID, ConstantsApp.PREFS_CONFIG);
        this.edtCardholderId.setText(lastCardHolderId);
    }

    public void submitGetCards(View view) {
        this.cardholderId = this.edtCardholderId.getText().toString();
        Log.d("FORM GetCards", this.cardholderId);

        getCardsTest();
    }

    private void getCardsTest() {

        IWalletApiService apiService = new WalletApiService.WalletApiBuilder(getApplicationContext())
                .baseUrlWallet(Helper.readPrefsString(getApplicationContext(), ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG))
                .authorization(Helper.readPrefsString(getApplicationContext(), ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG))
                .build();

        Handler handler = new Handler();

        try {
            apiService.getCards(cardholderId, new IWalletCallback<List<GetCardResponse>>() {
                @Override
                public void onResponse(Response<List<GetCardResponse>> response) {
                    Log.d(Constants.TAG, String.format("statusCode: %s ", response.code()));

                    if (response.isSuccessful()) {
                        List<GetCardResponse> getCardResponse = response.body();

                        for (GetCardResponse cardResponse : getCardResponse) {
                            Log.d(Constants.TAG, cardResponse.getId() + "");
                            Log.d(Constants.TAG, cardResponse.getNumber() + "");
                            Log.d(Constants.TAG, cardResponse.getCardholderName() + "");
                            Log.d(Constants.TAG, cardResponse.getActive() + "");
                        }

                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(getCardResponse);
                        showResult(jsonInString);


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
                            GetCardsActivity.this,
                            ImageDialogs.ERROR.ordinal()));
                }
            });
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage(), e);

            handler.post(() -> DialogUtils.showMessage(getString(R.string.title_error),
                    e.getMessage(),
                    GetCardsActivity.this,
                    ImageDialogs.ERROR.ordinal()));


            Toast.makeText(getApplicationContext(), R.string.infoLogcat, Toast.LENGTH_SHORT).show();
        }
    }

    private void showResult(String response) {
        Intent itResponse = new Intent(getApplicationContext(), ResponseActivity.class);
        itResponse.putExtra("result", response);
        startActivity(itResponse);
    }


}
