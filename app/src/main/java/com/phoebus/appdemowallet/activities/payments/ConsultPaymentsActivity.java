package com.phoebus.appdemowallet.activities.payments;

import android.content.Intent;
import android.os.Bundle;
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
import com.phoebus.libwallet.models.GetPaymentResponse;
import com.phoebus.libwallet.models.GetPaymentResponsePage;
import com.phoebus.libwallet.models.IWalletApiService;
import com.phoebus.libwallet.models.IWalletCallback;
import com.phoebus.libwallet.service.WalletApiService;
import com.phoebus.libwallet.utils.Constants;
import com.phoebus.libwallet.utils.ErrorUtils;
import com.phoebus.libwallet.utils.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.concrete.canarinho.watcher.MascaraNumericaTextWatcher;
import retrofit2.Response;

public class ConsultPaymentsActivity extends AppCompatActivity {

    private String initDateTime;
    private String finishDateTime;

    private EditText edtInitDateTime;
    private EditText edtFinishDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.consultPayments);

        setContentView(R.layout.activity_consult_payments);

        this.edtInitDateTime = this.findViewById(R.id.init_datetime);
        edtInitDateTime.addTextChangedListener(new MascaraNumericaTextWatcher("##/##/####"));
        this.edtFinishDateTime = this.findViewById(R.id.finish_datetime);
        edtFinishDateTime.addTextChangedListener(new MascaraNumericaTextWatcher("##/##/####"));

        setDefaultValues();
    }

    private void setDefaultValues() {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        String currentDateFormatBr = date.format(currentDate);

        edtInitDateTime.setText(currentDateFormatBr);
        edtFinishDateTime.setText(currentDateFormatBr);
    }

    public void submitConsultPayments(View view) {
        initDateTime = this.edtInitDateTime.getText().toString();
        finishDateTime = this.edtFinishDateTime.getText().toString();

        initDateTime = DateUtil.formatDate(this.initDateTime, "dd/MM/yyyy", "yyyy-MM-dd");
        finishDateTime = DateUtil.formatDate(this.finishDateTime, "dd/MM/yyyy", "yyyy-MM-dd");

        Log.d("FORM ConsultPayments", this.initDateTime + " " + this.finishDateTime);

        consultPaymentsTest();
    }

    private void consultPaymentsTest() {
        //TODO implementar função de consulta
        String BASE_URL_WALLET = Helper.readPrefsString(getApplicationContext(), ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG);
        String AUTHORIZATION_TOKEN = Helper.readPrefsString(getApplicationContext(), ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG);


        IWalletApiService apiService = new WalletApiService.WalletApiBuilder(getApplicationContext())
                .authorization(AUTHORIZATION_TOKEN)
                .baseUrlWallet(BASE_URL_WALLET)
                .build();

        try {
            apiService.listPayments(initDateTime, finishDateTime, null, null, new IWalletCallback<GetPaymentResponsePage>() {
                @Override
                public void onResponse(Response<GetPaymentResponsePage> response) {
                    Log.d(Constants.TAG, String.format("statusCode: %s ", response.code()));

                    if (response.isSuccessful()) {
                        GetPaymentResponsePage getPaymentResponsePage = response.body();

                        for(GetPaymentResponse getPaymentResponse : getPaymentResponsePage.getContent()) {
                            Log.d(Constants.TAG, getPaymentResponse.getAppTransactionId());
                            Log.d(Constants.TAG, getPaymentResponse.getPaymentId());
                            Log.d(Constants.TAG, getPaymentResponse.getMerchantPaymentId()+"");
                            Log.d(Constants.TAG, getPaymentResponse.getPaymentDateTime()+"");
                            Log.d(Constants.TAG, getPaymentResponse.getAuthorizationCode() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getCardId() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getAmount() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getMainProduct() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getSubProduct() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getInstalments() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getMerchantName() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getMerchantCity() + "");

                        }

                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(getPaymentResponsePage);
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

                    DialogUtils.showMessage("falha na requisição",
                            throwable.getMessage(),
                            getApplicationContext(),
                            ImageDialogs.ERROR.ordinal());
                }
            });
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage(), e);

            DialogUtils.showMessage("Exception",
                    e.getMessage(),
                    getApplicationContext(),
                    ImageDialogs.ERROR.ordinal());

            Toast.makeText(getApplicationContext(), R.string.infoLogcat, Toast.LENGTH_SHORT).show();
        }
    }

    private void showResult(String response) {
        Intent itResponse = new Intent(getApplicationContext(), ResponseActivity.class);
        itResponse.putExtra("result", response);
        startActivity(itResponse);
    }

}
