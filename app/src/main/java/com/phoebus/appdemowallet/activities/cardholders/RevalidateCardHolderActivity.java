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
import com.phoebus.libwallet.models.GeneralErrorResponse;
import com.phoebus.libwallet.models.IWalletApiService;
import com.phoebus.libwallet.models.IWalletCallback;
import com.phoebus.libwallet.models.SaveCardholderResponse;
import com.phoebus.libwallet.models.dtos.RevalidateCardholderRequestDTO;
import com.phoebus.libwallet.service.WalletApiService;
import com.phoebus.libwallet.utils.Constants;
import com.phoebus.libwallet.utils.ErrorUtils;
import com.phoebus.libwallet.utils.Helper;

import br.com.concrete.canarinho.watcher.MascaraNumericaTextWatcher;
import retrofit2.Response;


public class RevalidateCardHolderActivity extends AppCompatActivity {
    private String cellPhoneNumber;
    private String nationalDocument;

    private EditText edtCellPhoneNumber;
    private EditText edtNationalDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.revalidateCardHolder);

        setContentView(R.layout.activity_revalidate_card_holder);
        this.edtCellPhoneNumber = this.findViewById(R.id.cell_phone_number);
        edtCellPhoneNumber.addTextChangedListener(new MascaraNumericaTextWatcher("(##) # ####-####"));
        this.edtNationalDocument = this.findViewById(R.id.national_document);
        edtNationalDocument.addTextChangedListener(new MascaraNumericaTextWatcher("###.###.###-##"));

    }

    public void submitRevalidateCardHolder(View view) {
        this.cellPhoneNumber = this.edtCellPhoneNumber.getText().toString().replaceAll("[() -]", "");
        this.nationalDocument = this.edtNationalDocument.getText().toString().replaceAll("[.-]", "");

        revalidateCardHolderTest();
    }

    public void revalidateCardHolderTest() {
        String BASE_URL_WALLET = Helper.readPrefsString(getApplicationContext(), ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG);
        String AUTHORIZATION_TOKEN = Helper.readPrefsString(getApplicationContext(), ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG);

        RevalidateCardholderRequestDTO revalidateCardholderRequestDTO = new RevalidateCardholderRequestDTO(
                this.cellPhoneNumber,
                Helper.readPrefsString(getApplicationContext(), ConstantsApp.NOTIFICATION_TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG)
        );

        IWalletApiService apiService = new WalletApiService
                .WalletApiBuilder(getApplicationContext())
                .baseUrlWallet(BASE_URL_WALLET)
                .authorization(AUTHORIZATION_TOKEN)
                .build();

        Handler handler = new Handler();

        try {
            apiService.revalidateCardHolder(this.nationalDocument, revalidateCardholderRequestDTO, new IWalletCallback<SaveCardholderResponse>() {
                @Override
                public void onResponse(Response<SaveCardholderResponse> response) {
                    Log.d(Constants.TAG, response.toString());
                    if (response.isSuccessful()) {
                        SaveCardholderResponse saveCardholderResponse = response.body();
                        Log.d(Constants.TAG, saveCardholderResponse.getCardholderId());
                        Helper.writePrefs(getApplicationContext(), ConstantsApp.CARD_HOLDER_ID, saveCardholderResponse.getCardholderId(), ConstantsApp.PREFS_CONFIG);

                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(saveCardholderResponse);
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
                            RevalidateCardHolderActivity.this,
                            ImageDialogs.ERROR.ordinal()));

                }
            });
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage(), e);

            handler.post(() -> DialogUtils.showMessage(getString(R.string.title_error),
                    e.getMessage(),
                    RevalidateCardHolderActivity.this,
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
