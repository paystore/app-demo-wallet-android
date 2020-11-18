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
import com.phoebus.appdemowallet.utils.DateUtil;
import com.phoebus.appdemowallet.utils.DialogUtils;
import com.phoebus.appdemowallet.utils.ImageDialogs;
import com.phoebus.libwallet.models.CreateCardRequest;
import com.phoebus.libwallet.models.CreateCardResponse;
import com.phoebus.libwallet.models.FieldValidationErrorResponse;
import com.phoebus.libwallet.models.GeneralErrorResponse;
import com.phoebus.libwallet.models.IWalletApiService;
import com.phoebus.libwallet.models.IWalletCallback;
import com.phoebus.libwallet.service.WalletApiService;
import com.phoebus.libwallet.utils.Constants;
import com.phoebus.libwallet.utils.ErrorUtils;
import com.phoebus.libwallet.utils.Helper;

import br.com.concrete.canarinho.watcher.MascaraNumericaTextWatcher;
import retrofit2.Response;

public class SaveCardActivity extends AppCompatActivity {

    private String cardholderId;
    private String number;
    private String cardHolderName;
    private String dueDate;
    private String cvv;

    private EditText edtCardholderId;
    private EditText edtNumber;
    private EditText edtCardHolderName;
    private EditText edtDueDate;
    private EditText edtCvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.saveCard);

        setContentView(R.layout.activity_save_card);
        edtCardholderId = this.findViewById(R.id.card_holder_id);
        edtNumber = this.findViewById(R.id.number);
        edtCardHolderName = this.findViewById(R.id.card_holder_name);
        edtDueDate = this.findViewById(R.id.due_date);
        edtDueDate.addTextChangedListener(new MascaraNumericaTextWatcher("##/####"));
        edtCvv = this.findViewById(R.id.cvv);
    }

    public void submitSaveCard(View view) {
        cardholderId = edtCardholderId.getText().toString();
        number = this.edtNumber.getText().toString();
        cardHolderName = this.edtCardHolderName.getText().toString();

        dueDate = this.edtDueDate.getText().toString();
        dueDate = DateUtil.formatDate(this.dueDate, "MM/yyyy", "yyyy-MM");

        cvv = this.edtCvv.getText().toString();
        cvv = !cvv.isEmpty() ? cvv : null;

        saveCardTest();
    }

    private void saveCardTest() {

        IWalletApiService apiService = new WalletApiService.WalletApiBuilder(getApplicationContext())
                .baseUrlWallet(Helper.readPrefsString(getApplicationContext(), ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG))
                .authorization(Helper.readPrefsString(getApplicationContext(), ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG))
                .build();

        Handler handler = new Handler();

        CreateCardRequest createCardRequest = new CreateCardRequest(cardholderId, number, cardHolderName, dueDate, cvv);
        try {
            apiService.saveCard(createCardRequest, new IWalletCallback<CreateCardResponse>() {
                @Override
                public void onResponse(Response<CreateCardResponse> response) {
                    Log.d(Constants.TAG, String.format("statusCode: %s ", response.code()));

                    if (response.isSuccessful()) {
                        CreateCardResponse createCardResponse = response.body();

                        Helper.writePrefs(getApplicationContext(), ConstantsApp.CARD_ID, createCardResponse.getId(), ConstantsApp.PREFS_CONFIG);
                        Log.d(Constants.TAG, createCardResponse.getId());

                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(createCardResponse);
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
                            SaveCardActivity.this,
                            ImageDialogs.ERROR.ordinal()));

                }
            });
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage(), e);

            handler.post(() -> DialogUtils.showMessage(getString(R.string.title_error),
                    e.getMessage(),
                    SaveCardActivity.this,
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
