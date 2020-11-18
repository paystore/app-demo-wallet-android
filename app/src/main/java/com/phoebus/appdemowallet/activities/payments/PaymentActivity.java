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
import com.phoebus.appdemowallet.utils.QrCodeUtils;
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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private String qrCode;
    private String cardId;
    private String cardHolderId;
    private String appTransactionId;
    private String cvv;

    private EditText edtQrCode;
    private EditText edtCardId;
    private EditText edtCardHolderId;
    private EditText edtAppTransactionId;
    private EditText edtCvv;

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

        setDefaultValues();
    }

    private void setDefaultValues() {
        this.edtQrCode.setText(QrCodeUtils.sample());

        String lastCardId = Helper.readPrefsString(getApplicationContext(), ConstantsApp.CARD_ID, ConstantsApp.PREFS_CONFIG);
        this.edtCardId.setText(lastCardId);

        String lastCardHolderId = Helper.readPrefsString(getApplicationContext(), ConstantsApp.CARD_HOLDER_ID, ConstantsApp.PREFS_CONFIG);
        this.edtCardHolderId.setText(lastCardHolderId);

        this.edtAppTransactionId.setText("123456ABCDEF");
        this.edtCvv.setText("999");
    }

    public void submitPayment(View view){
        this.qrCode = this.edtQrCode.getText().toString();
        this.cardId = this.edtCardId.getText().toString();
        this.cardHolderId = this.edtCardHolderId.getText().toString();
        this.appTransactionId = this.edtAppTransactionId.getText().toString();
        this.cvv = this.edtCvv.getText().toString();

        Log.d(Constants.TAG, "Dados de pagamento: {"
                + "\n  QRCode: " + this.qrCode
                + "\n  cardIf: " + this.cardId
                + "\n  card: " + this.cardHolderId
                + "\n  appTransactionId: " + this.appTransactionId
                + "\n  cvv: " + this.cvv
                +"\n}");

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
        try {
            String BASE_URL_WALLET = Helper.readPrefsString(getApplicationContext(), ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG);
            String AUTHORIZATION_TOKEN = Helper.readPrefsString(getApplicationContext(), ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG);

            IWalletApiService apiService = new WalletApiService.WalletApiBuilder(getApplicationContext())
                    .baseUrlWallet(BASE_URL_WALLET)
                    .authorization(AUTHORIZATION_TOKEN)
                    .build();

            // chama e executa a consulta com os dados do QRCode
            QRCodeInfo qrCodeInfo = apiService.getQRCodeInfo(this.qrCode);
            QrCodeUtils.log(qrCodeInfo);

            //após o usuário confirmar em tela será chamado o pagamento
            CreatePaymentRequestDTO createPaymentRequestDTO = new CreatePaymentRequestDTO();
            createPaymentRequestDTO.setAppTransactionId(this.appTransactionId); // id generate app other.
            createPaymentRequestDTO.setQrcodeData(this.qrCode);
            createPaymentRequestDTO.setCardId(this.cardId); //input users or preferred card
            createPaymentRequestDTO.setCvv(this.cvv); //input for users.
            createPaymentRequestDTO.setCardholderId(this.cardHolderId);

            String ip = getDeviceIPAddress(true);
            createPaymentRequestDTO.setIp(ip);

            String notificationToken = Helper.readPrefsString(getApplicationContext(), ConstantsApp.NOTIFICATION_TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG);
            createPaymentRequestDTO.setNotificationToken(notificationToken);

            //chama o método de pagamento
            apiService.startPayment(createPaymentRequestDTO, new IWalletCallback<CreatePaymentResponse>() {
                @Override
                public void onResponse(Response<CreatePaymentResponse> response) {
                    Toast.makeText(getApplicationContext(), R.string.infoLogcat, Toast.LENGTH_SHORT).show();
                    Log.d(Constants.TAG, String.format("startPayment statusCode: %s ", response.code()));

                    if (response.isSuccessful()) {
                        CreatePaymentResponse createPaymentResponse = response.body();

                        Log.d(Constants.TAG, "startPayment response: {"
                                + "\n authorizationCode: " + createPaymentResponse.getAuthorizationCode()
                                + "\n merchantPaymentId: " + createPaymentResponse.getMerchantPaymentId()
                                + "\n paymentDateTime: " + createPaymentResponse.getPaymentDateTime()
                                + "\n paymentId: " + createPaymentResponse.getPaymentId()
                                + "\n}");

                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(createPaymentResponse);
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
                    Toast.makeText(getApplicationContext(), R.string.infoLogcat, Toast.LENGTH_SHORT).show();

                    DialogUtils.showMessage("falha na requisição",
                            throwable.getMessage(),
                            PaymentActivity.this,
                            ImageDialogs.ERROR.ordinal());

                    Log.e(Constants.TAG, throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.infoLogcat, Toast.LENGTH_SHORT).show();
            Log.e(Constants.TAG, e.getMessage(), e);

            DialogUtils.showMessage("falha ao enviar pagamento",
                    e.getMessage(),
                    PaymentActivity.this,
                    ImageDialogs.ERROR.ordinal());
        }

    }

    private void showResult(String response) {
        Intent itResponse = new Intent(getApplicationContext(), ResponseActivity.class);
        itResponse.putExtra("result", response);
        startActivity(itResponse);
    }

    public static String getDeviceIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : networkInterfaces) {
                List<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress inetAddress : inetAddresses) {
                    if (!inetAddress.isLoopbackAddress()) {

                        String sAddr = inetAddress.getHostAddress().toUpperCase();

                        boolean isIPv4 = inetAddress instanceof Inet4Address;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;

                        } else {
                            if (!isIPv4) {
                                // drop ip6 port suffix
                                int delim = sAddr.indexOf('%');
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

}
