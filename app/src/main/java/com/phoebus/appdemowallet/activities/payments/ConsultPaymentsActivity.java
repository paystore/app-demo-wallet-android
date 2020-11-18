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
import com.phoebus.libwallet.models.ListPaymentRequest;
import com.phoebus.libwallet.service.WalletApiService;
import com.phoebus.libwallet.utils.Constants;
import com.phoebus.libwallet.utils.ErrorUtils;
import com.phoebus.libwallet.utils.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.concrete.canarinho.watcher.MascaraNumericaTextWatcher;
import retrofit2.Response;

public class ConsultPaymentsActivity extends AppCompatActivity {

    private String startDate;
    private String finishDate;
    private String status = null;
    private String startAmount = null;
    private String finishAmount = null;
    private String cardId = null;
    private Integer pageSize = null;
    private Integer page = null;

    private EditText edtInitDateTime;
    private EditText edtFinishDateTime;
    private EditText edtStatus;
    private EditText edtStartAmount;
    private EditText edtFinishAmount;
    private EditText edtCardId;
    private EditText edtPageSize;
    private EditText edtPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.consultPayments);

        setContentView(R.layout.activity_consult_payments);

        this.edtInitDateTime = this.findViewById(R.id.init_datetime);
        edtInitDateTime.addTextChangedListener(new MascaraNumericaTextWatcher("##/##/####"));
        this.edtFinishDateTime = this.findViewById(R.id.finish_datetime);
        edtFinishDateTime.addTextChangedListener(new MascaraNumericaTextWatcher("##/##/####"));

        this.edtStatus = this.findViewById(R.id.consult_status);
        this.edtStartAmount = this.findViewById(R.id.consult_start_amount);
        this.edtFinishAmount = this.findViewById(R.id.consult_finish_amount);
        this.edtCardId = this.findViewById(R.id.consult_card_id);
        this.edtPageSize = this.findViewById(R.id.consult_page_size);
        this.edtPage = this.findViewById(R.id.consult_page);

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
        startDate = this.edtInitDateTime.getText().toString();
        finishDate = this.edtFinishDateTime.getText().toString();

        if (!this.edtStatus.getText().toString().isEmpty())
            status = this.edtStatus.getText().toString();

        if (!this.edtStartAmount.getText().toString().isEmpty())
            startAmount = this.edtStartAmount.getText().toString();

        if (!this.edtFinishAmount.getText().toString().isEmpty())
            finishAmount = this.edtFinishAmount.getText().toString();

        if (!this.edtCardId.getText().toString().isEmpty())
            cardId = this.edtCardId.getText().toString();

        if (!this.edtPageSize.getText().toString().isEmpty())
            pageSize = Integer.parseInt(this.edtPageSize.getText().toString());

        if (!this.edtPage.getText().toString().isEmpty())
            page = Integer.parseInt(this.edtPage.getText().toString());

        startDate = DateUtil.formatDate(this.startDate, "dd/MM/yyyy", "yyyy-MM-dd");
        finishDate = DateUtil.formatDate(this.finishDate, "dd/MM/yyyy", "yyyy-MM-dd");

        Log.d("FORM ConsultPayments", this.startDate + " " + this.finishDate);

        consultPaymentsTest();
    }

    private void consultPaymentsTest() {
        String BASE_URL_WALLET = Helper.readPrefsString(getApplicationContext(), ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG);
        String AUTHORIZATION_TOKEN = Helper.readPrefsString(getApplicationContext(), ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG);


        IWalletApiService apiService = new WalletApiService.WalletApiBuilder(getApplicationContext())
                .authorization(AUTHORIZATION_TOKEN)
                .baseUrlWallet(BASE_URL_WALLET)
                .build();

        try {
            ListPaymentRequest listPaymentRequest = new ListPaymentRequest(startDate, finishDate,
                    status, startAmount, finishAmount, cardId, pageSize,
                    page);
            apiService.listPayments(listPaymentRequest, new IWalletCallback<GetPaymentResponsePage>() {
                @Override
                public void onResponse(Response<GetPaymentResponsePage> response) {
                    Log.d(Constants.TAG, String.format("statusCode: %s ", response.code()));

                    if (response.isSuccessful()) {
                        GetPaymentResponsePage getPaymentResponsePage = response.body();

                        for(GetPaymentResponse getPaymentResponse : getPaymentResponsePage.getContent()) {
                            Log.d(Constants.TAG, getPaymentResponse.getAppTransactionId());
                            Log.d(Constants.TAG, getPaymentResponse.getPaymentId());
                            Log.d(Constants.TAG, getPaymentResponse.getStatus()+"");
                            Log.d(Constants.TAG, getPaymentResponse.getPaymentDateTime()+"");
                            Log.d(Constants.TAG, getPaymentResponse.getAuthorizationCode() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getCardId() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getAmount() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getProduct() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getProductType() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getInstallments() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getMerchantName() + "");
                            Log.d(Constants.TAG, getPaymentResponse.getMerchantCity() + "");

                        }

                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(getPaymentResponsePage);
                        showResult(getPaymentResponsePage, jsonInString);


                    } else {
                        GeneralErrorResponse generalErrorResponse = ErrorUtils.parseError(response);

                        if (generalErrorResponse != null) {

                            Gson gson = new Gson();
                            String jsonInString = gson.toJson(generalErrorResponse);
                            showErrorResult(jsonInString);

                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.e(Constants.TAG, throwable.getMessage());

                    DialogUtils.showMessage("falha na requisição",
                            throwable.getMessage(),
                            ConsultPaymentsActivity.this,
                            ImageDialogs.ERROR.ordinal());
                }
            });
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage(), e);

            DialogUtils.showMessage("Exception",
                    e.getMessage(),
                    ConsultPaymentsActivity.this,
                    ImageDialogs.ERROR.ordinal());

            Toast.makeText(getApplicationContext(), R.string.infoLogcat, Toast.LENGTH_SHORT).show();
        }
    }

    private void showResult(GetPaymentResponsePage response, String responseString) {
        List<GetPaymentResponse> content = response.getContent();
        if(content.size() > 0){
            Intent itResponse = new Intent(getApplicationContext(), ListPaymentActivity.class);
            itResponse.putExtra("result", responseString);
            startActivity(itResponse);
        }else{
            Toast.makeText(getApplicationContext(), "Nenhum pagamento encontrado", Toast.LENGTH_LONG).show();
        }
    }

    private void showErrorResult(String response) {
        Intent itResponse = new Intent(getApplicationContext(), ResponseActivity.class);
        itResponse.putExtra("result", response);
        startActivity(itResponse);
    }

}
