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
import com.phoebus.appdemowallet.utils.DateUtil;
import com.phoebus.appdemowallet.utils.DialogUtils;
import com.phoebus.appdemowallet.utils.ImageDialogs;
import com.phoebus.libwallet.models.FieldValidationErrorResponse;
import com.phoebus.libwallet.models.GeneralErrorResponse;
import com.phoebus.libwallet.models.IWalletApiService;
import com.phoebus.libwallet.models.IWalletCallback;
import com.phoebus.libwallet.models.SaveCardholderResponse;
import com.phoebus.libwallet.models.dtos.CreateCardholderRequestDTO;
import com.phoebus.libwallet.service.WalletApiService;
import com.phoebus.libwallet.utils.Constants;
import com.phoebus.libwallet.utils.ErrorUtils;
import com.phoebus.libwallet.utils.Helper;

import br.com.concrete.canarinho.watcher.MascaraNumericaTextWatcher;
import retrofit2.Response;

public class CreateCardHolderActivity extends AppCompatActivity {
    private String fullName;
    private String email;
    private String nationalDocument;
    private String cellPhoneNumber;
    private String birthDate;

    private EditText edtFullName;
    private EditText edtEmail;
    private EditText edtNationalDocument;
    private EditText edtCellPhoneNumber;
    private EditText edtBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.createCardHolder);

        setContentView(R.layout.activity_create_card_holder);

        this.edtFullName = this.findViewById(R.id.full_name);
        this.edtEmail = this.findViewById(R.id.email);

        this.edtNationalDocument = this.findViewById(R.id.national_document);
        edtNationalDocument.addTextChangedListener(new MascaraNumericaTextWatcher("###.###.###-##"));

        this.edtCellPhoneNumber = this.findViewById(R.id.cell_phone_number);
        edtCellPhoneNumber.addTextChangedListener(new MascaraNumericaTextWatcher("(##) # ####-####"));

        this.edtBirthDate = this.findViewById(R.id.birth_date);
        edtBirthDate.addTextChangedListener(new MascaraNumericaTextWatcher("##/##/####"));

        setDefaultValues();

    }

    private void setDefaultValues() {

        String name = Helper.readPrefsString(getApplicationContext(), ConstantsApp.CREATE_CARD_HOLDER_FULL_NAME, ConstantsApp.PREFS_CONFIG);
        if (name == null || name.isEmpty()) {
            edtFullName.setText("Jhon Doe");
            edtEmail.setText("jhondoe@gmail.com");
            edtNationalDocument.setText("80037203037");
            edtCellPhoneNumber.setText("83987654321");
            edtBirthDate.setText("10/05/1990");
        } else {
            edtFullName.setText(Helper.readPrefsString(getApplicationContext(), ConstantsApp.CREATE_CARD_HOLDER_FULL_NAME, ConstantsApp.PREFS_CONFIG));
            edtEmail.setText(Helper.readPrefsString(getApplicationContext(), ConstantsApp.CREATE_CARD_HOLDER_EMAIL, ConstantsApp.PREFS_CONFIG));
            edtNationalDocument.setText(Helper.readPrefsString(getApplicationContext(), ConstantsApp.CREATE_CARD_HOLDER_NATIONAL_ID, ConstantsApp.PREFS_CONFIG));
            edtCellPhoneNumber.setText(Helper.readPrefsString(getApplicationContext(), ConstantsApp.CREATE_CARD_HOLDER_CELL_PHONE, ConstantsApp.PREFS_CONFIG));

            Log.d(Constants.TAG, Helper.readPrefsString(getApplicationContext(), ConstantsApp.CREATE_CARD_HOLDER_BIRT_DATE, ConstantsApp.PREFS_CONFIG));
            String strDate = Helper.readPrefsString(getApplicationContext(), ConstantsApp.CREATE_CARD_HOLDER_BIRT_DATE, ConstantsApp.PREFS_CONFIG);
            strDate = strDate.substring(8, 10)
                    + strDate.substring(5, 7)
                    + strDate.substring(0, 4);
            edtBirthDate.setText(strDate);
        }
    }

    public void submitCreateCardHolder(View view) {
        fullName = this.edtFullName.getText().toString();
        email = this.edtEmail.getText().toString();
        nationalDocument = this.edtNationalDocument.getText().toString().replaceAll("[.-]", "");
        cellPhoneNumber = this.edtCellPhoneNumber.getText().toString().replaceAll("[() -]", "");

        birthDate = this.edtBirthDate.getText().toString();
        birthDate = DateUtil.formatDate(this.birthDate, "dd/MM/yyyy", "yyyy-MM-dd");

        Log.d("FORM CreateCardHolder", this.fullName + " " + this.email + " " + this.nationalDocument + " " + this.cellPhoneNumber + " " + this.birthDate);

        createCardHolderTest();


    }

    private void createCardHolderTest() {
        String BASE_URL_WALLET = Helper.readPrefsString(getApplicationContext(), ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG);
        String AUTHORIZATION_TOKEN = Helper.readPrefsString(getApplicationContext(), ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG);


        String notificationToken = Helper.readPrefsString(getApplicationContext(), ConstantsApp.NOTIFICATION_TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG);


        CreateCardholderRequestDTO createCardholderRequestDTO = new CreateCardholderRequestDTO(fullName,
                email, nationalDocument,
                birthDate, cellPhoneNumber, notificationToken);

        IWalletApiService apiService = new WalletApiService
                .WalletApiBuilder(getApplicationContext())
                .baseUrlWallet(BASE_URL_WALLET)
                .authorization(AUTHORIZATION_TOKEN)
                .build();

        Handler handler = new Handler();

        try {

            apiService.createCardholder(createCardholderRequestDTO, new IWalletCallback<SaveCardholderResponse>() {

                @Override
                public void onResponse(Response<SaveCardholderResponse> response) {
                    Log.d(Constants.TAG, String.format("statusCode: %s ", response.code()));

                    if (response.isSuccessful()) {
                        SaveCardholderResponse saveCardholderResponse = response.body();

                        Log.d(Constants.TAG, saveCardholderResponse.getCardholderId());

                        Helper.writePrefs(getApplicationContext(), ConstantsApp.CARD_HOLDER_ID, saveCardholderResponse.getCardholderId(), ConstantsApp.PREFS_CONFIG);

                        //save cardholder in preferences
                        Helper.writePrefs(getApplicationContext(), ConstantsApp.CREATE_CARD_HOLDER_FULL_NAME, createCardholderRequestDTO.getFullName(), ConstantsApp.PREFS_CONFIG);
                        Helper.writePrefs(getApplicationContext(), ConstantsApp.CREATE_CARD_HOLDER_BIRT_DATE, createCardholderRequestDTO.getBirthDate(), ConstantsApp.PREFS_CONFIG);
                        Helper.writePrefs(getApplicationContext(), ConstantsApp.CREATE_CARD_HOLDER_CELL_PHONE, createCardholderRequestDTO.getCellphoneNumber(), ConstantsApp.PREFS_CONFIG);
                        Helper.writePrefs(getApplicationContext(), ConstantsApp.CREATE_CARD_HOLDER_EMAIL, createCardholderRequestDTO.getEmail(), ConstantsApp.PREFS_CONFIG);
                        Helper.writePrefs(getApplicationContext(), ConstantsApp.CREATE_CARD_HOLDER_NATIONAL_ID, createCardholderRequestDTO.getNationalId(), ConstantsApp.PREFS_CONFIG);

                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(saveCardholderResponse);
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
                            CreateCardHolderActivity.this,
                            ImageDialogs.ERROR.ordinal()));
               }
            });
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage(), e);

            handler.post(() -> DialogUtils.showMessage(getString(R.string.title_error),
                    e.getMessage(),
                    CreateCardHolderActivity.this,
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
