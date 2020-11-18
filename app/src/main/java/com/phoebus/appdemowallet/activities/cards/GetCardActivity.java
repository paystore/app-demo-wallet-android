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

import retrofit2.Response;


public class GetCardActivity extends AppCompatActivity {
    private String cardId;

    private EditText edtCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.getCard);

        setContentView(R.layout.activity_get_card);
        this.edtCardId = this.findViewById(R.id.card_id);
        String lastCardId = Helper.readPrefsString(getApplicationContext(), ConstantsApp.CARD_ID, ConstantsApp.PREFS_CONFIG);
        this.edtCardId.setText(lastCardId);

    }

    public void submitGetCard(View view){
        this.cardId = this.edtCardId.getText().toString();
        Log.d("FORM GetCard", this.cardId);

        getCardTest();
    }

    private void getCardTest() {

        IWalletApiService apiService = new WalletApiService.WalletApiBuilder(getApplicationContext())
                .baseUrlWallet(Helper.readPrefsString(getApplicationContext(), ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG))
                .authorization(Helper.readPrefsString(getApplicationContext(), ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG))
                .build();

        Handler handler = new Handler();

        try {
            apiService.getCard(cardId, new IWalletCallback<GetCardResponse>() {
                @Override
                public void onResponse(Response<GetCardResponse> response) {
                    Log.d(Constants.TAG, String.format("statusCode: %s ", response.code()));

                    if (response.isSuccessful()) {
                        GetCardResponse getCardResponse = response.body();

                        Log.d(Constants.TAG, getCardResponse.getId());
                        Log.d(Constants.TAG, getCardResponse.getNumber());
                        Log.d(Constants.TAG, getCardResponse.getCardholderName());
                        Log.d(Constants.TAG, getCardResponse.getActive() + "");

                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(getCardResponse);
                        showResult(jsonInString);


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
                            GetCardActivity.this,
                            ImageDialogs.ERROR.ordinal()));
                }
            });
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage(), e);

            handler.post(() -> DialogUtils.showMessage(getString(R.string.title_error),
                    e.getMessage(),
                    GetCardActivity.this,
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
