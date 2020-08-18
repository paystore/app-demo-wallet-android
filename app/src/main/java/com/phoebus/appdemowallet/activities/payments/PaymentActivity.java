package com.phoebus.appdemowallet.activities.payments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.phoebus.appdemowallet.R;
import com.phoebus.appdemowallet.activities.ResponseActivity;
import com.phoebus.appdemowallet.utils.ConstantsApp;
import com.phoebus.appdemowallet.utils.DialogUtils;
import com.phoebus.appdemowallet.utils.ImageDialogs;
import com.phoebus.libwallet.models.CreatePaymentResponse;
import com.phoebus.libwallet.models.GeneralErrorResponse;
import com.phoebus.libwallet.models.IWalletApiService;
import com.phoebus.libwallet.models.IWalletCallback;
import com.phoebus.libwallet.models.dtos.CreatePaymentRequestDTO;
import com.phoebus.libwallet.models.qrcode.QRCodeInfo;
import com.phoebus.libwallet.service.WalletApiService;
import com.phoebus.libwallet.utils.Constants;
import com.phoebus.libwallet.utils.ErrorUtils;
import com.phoebus.libwallet.utils.Helper;

import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private String qrCode;
    private String cardId;
    private String cardHolderId;
    private String appTransactionId;
    private String cvv;
    private Integer installment;

    private EditText edtQrCode;
    private EditText edtCardId;
    private EditText edtCardHolderId;
    private EditText edtAppTransactionId;
    private EditText edtCvv;
    private EditText edtInstallment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.payment);

        setContentView(R.layout.activity_payment);

        this.edtQrCode = this.findViewById(R.id.qr_code);
        this.edtCardId = this.findViewById(R.id.card_id);
        this.edtCardHolderId = this.findViewById(R.id.card_holder_id);
        this.edtAppTransactionId = this.findViewById(R.id.app_transaction_id);
        this.edtCvv = this.findViewById(R.id.cvv);
        this.edtInstallment = this.findViewById(R.id.installment);

        setDefaultValues();
    }

    private void setDefaultValues() {
        this.edtQrCode.setText("00020101021226440008PayStore0116123456789012000102082009130352040000530398654120000000001005802BR5909SENFFCARD6011CURITIBA PR 801050037\"https://www.senffcard.com.br/qrcode\"011613050329197F190A0212150518113349030410000404000105020006020163049872");
        this.edtCardId.setText("56aab9ec-da5e-47ce-9a45-3acb6ed6b07e");
        this.edtCardHolderId.setText("5f1706f9714e87610d50b8bd");
        this.edtAppTransactionId.setText("123456ABCDEF");
        this.edtCvv.setText("999");
        this.edtInstallment.setText("1");
    }

    public void submitPayment(View view){
        this.qrCode = this.edtQrCode.getText().toString();
        this.cardId = this.edtCardId.getText().toString();
        this.cardHolderId = this.edtCardHolderId.getText().toString();
        this.appTransactionId = this.edtAppTransactionId.getText().toString();
        this.cvv = this.edtCvv.getText().toString();
        this.installment = Integer.parseInt(this.edtInstallment.getText().toString());
        Log.d("FORM payment", this.qrCode+" "+this.cardId+" "+this.cardHolderId+" "+this.appTransactionId+" "+this.cvv+" "+this.installment);

        startPayment();
    }

    public void readQrCode(View view){
        IntentIntegrator intentIntegrator = new IntentIntegrator(PaymentActivity.this);

        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setCameraId(0);//0 camera traseira 1 camera frontal
        intentIntegrator.setPrompt("Aproxime o QR code para leitura");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() != null){
                this.edtQrCode.setText(result.getContents());
            }else{
                Toast.makeText(getApplicationContext(), "Erro na leitura do QR code", Toast.LENGTH_SHORT);
            }

        }

    }

    private void startPayment() {
        String BASE_URL_WALLET = Helper.readPrefsString(getApplicationContext(), ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG);
        String AUTHORIZATION_TOKEN = Helper.readPrefsString(getApplicationContext(), ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG);

        IWalletApiService apiService = new WalletApiService.WalletApiBuilder(getApplicationContext())
                .baseUrlWallet(BASE_URL_WALLET)
                .authorization(AUTHORIZATION_TOKEN)
                .build();

        // chama e executa a consulta com os dados do QRCode
        QRCodeInfo qrCodeInfo = null;
        try {
            qrCodeInfo = apiService.getQRCodeInfo(this.qrCode);

            Log.d(Constants.TAG, qrCodeInfo.getPayloadFormatIndicator());
            Log.d(Constants.TAG, qrCodeInfo.getPointOfInitiationMethod());
            Log.d(Constants.TAG, qrCodeInfo.getMerchantAccountInformation().getGloballyUniqueIdentifier());
            Log.d(Constants.TAG, qrCodeInfo.getMerchantAccountInformation().getMerchantAccountInformation());
            Log.d(Constants.TAG, qrCodeInfo.getMerchantAccountInformation().getLogicNumber());
            Log.d(Constants.TAG, qrCodeInfo.getMerchantCategoryCode());
            Log.d(Constants.TAG, qrCodeInfo.getTransactionCurrency());
            Log.d(Constants.TAG, qrCodeInfo.getTransactionAmount());
            Log.d(Constants.TAG, qrCodeInfo.getCountryCode());
            Log.d(Constants.TAG, qrCodeInfo.getMerchantName());
            Log.d(Constants.TAG, qrCodeInfo.getMerchantCity());
            Log.d(Constants.TAG, qrCodeInfo.getTransactionInformations().getTransactionGloballyUniqueIdentifier());
            Log.d(Constants.TAG, qrCodeInfo.getTransactionInformations().getTransactionID());
            Log.d(Constants.TAG, qrCodeInfo.getTransactionInformations().getTransactionDate());
            Log.d(Constants.TAG, qrCodeInfo.getTransactionInformations().getMainProduct());
            Log.d(Constants.TAG, qrCodeInfo.getTransactionInformations().getSubProduct());
            Log.d(Constants.TAG, qrCodeInfo.getTransactionInformations().getPaymentInstallments());
            Log.d(Constants.TAG, qrCodeInfo.getTransactionInformations().getTransactionType());
            Log.d(Constants.TAG, qrCodeInfo.getCrc());
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage(), e);
        }

        try {
            //após o usuário confirmar em tela será chamado o pagamento
            //chama o método de pagamento
            CreatePaymentRequestDTO createPaymentRequestDTO = new CreatePaymentRequestDTO();
            createPaymentRequestDTO.setAppTransactionId(this.appTransactionId); // id generate app other.
            createPaymentRequestDTO.setCardholderId(this.cardHolderId);
            createPaymentRequestDTO.setQrcodeData(this.qrCode);
            createPaymentRequestDTO.setCardId(this.cardId); //input users or preferred card
            createPaymentRequestDTO.setCvv(this.cvv); //input for users.
            createPaymentRequestDTO.setAmount(qrCodeInfo.getTransactionAmount()); //info qrcode data
            createPaymentRequestDTO.setMainProduct(qrCodeInfo.getTransactionInformations().getMainProduct()); ////info qrcode data
            createPaymentRequestDTO.setSubProduct(qrCodeInfo.getTransactionInformations().getSubProduct()); ////info qrcode data
            createPaymentRequestDTO.setInstallments(this.installment); //input for users.

            String notificationToken = Helper.readPrefsString(getApplicationContext(), ConstantsApp.NOTIFICATION_TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG);
            createPaymentRequestDTO.setNotificationToken(notificationToken);


            apiService.startPayment(createPaymentRequestDTO, new IWalletCallback<CreatePaymentResponse>() {
                @Override
                public void onResponse(Response<CreatePaymentResponse> response) {
                    Toast.makeText(getApplicationContext(), R.string.infoLogcat, Toast.LENGTH_SHORT).show();
                    Log.d(Constants.TAG, String.format("statusCode: %s ", response.code()));

                    if (response.isSuccessful()) {
                        CreatePaymentResponse createPaymentResponse = response.body();

                        Log.d(Constants.TAG, createPaymentResponse.getPaymentId());
                        Log.d(Constants.TAG, createPaymentResponse.getAuthorizationCode());
                        Log.d(Constants.TAG, createPaymentResponse.getMerchantPaymentId());
                        Log.d(Constants.TAG, createPaymentResponse.getPaymentDateTime());

                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(createPaymentResponse);
                        showResult(jsonInString);

                    } else {
                        GeneralErrorResponse generalErrorResponse = ErrorUtils.parseError(response);
                        if (generalErrorResponse != null) {
                            Log.d(Constants.TAG, generalErrorResponse.getTimestamp());
                            Log.d(Constants.TAG, generalErrorResponse.getMessage());
                            Log.d(Constants.TAG, generalErrorResponse.getStatus().toString());
                            Log.d(Constants.TAG, generalErrorResponse.getError());
                            Log.d(Constants.TAG, generalErrorResponse.getErrors().get(0).getDefaultMessage());
                            Log.d(Constants.TAG, generalErrorResponse.getErrors().get(0).getField());

                            Gson gson = new Gson();
                            String jsonInString = gson.toJson(generalErrorResponse);
                            showResult(jsonInString);

                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Toast.makeText(getApplicationContext(), R.string.infoLogcat, Toast.LENGTH_SHORT).show();

                    DialogUtils.showMessage("falha na requisição",
                            throwable.getMessage(),
                            getApplicationContext(),
                            ImageDialogs.ERROR.ordinal());

                    Log.e(Constants.TAG, throwable.getMessage());
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
