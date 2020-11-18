package com.phoebus.appdemowallet.activities.payments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.phoebus.appdemowallet.R;
import com.phoebus.appdemowallet.utils.QrCodeUtils;
import com.phoebus.libwallet.models.IWalletApiService;
import com.phoebus.libwallet.models.qrcode.QRCodeInfo;
import com.phoebus.libwallet.service.WalletApiService;
import com.phoebus.libwallet.utils.Constants;


public class ReadQrCodeActivity extends AppCompatActivity {
    private String qrCode;

    private EditText edtQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.readInformationCapturedWithQrCode);

        setContentView(R.layout.activity_read_qr_code);

        this.edtQrCode = this.findViewById(R.id.qr_code);
        this.edtQrCode.setText(QrCodeUtils.sample());
        Log.d("1 - QR code", this.edtQrCode.getText().toString());
    }

    public void submitReadQrCode(View view){
        this.qrCode = this.edtQrCode.getText().toString();
        getQrCodeInfo();
    }

    public void readQrCode(View view){
        IntentIntegrator intentIntegrator = new IntentIntegrator(ReadQrCodeActivity.this);

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
                Log.d("2 - QR code", result.getContents());
            }else{
                Toast.makeText(getApplicationContext(), "Erro na leitura do QR code", Toast.LENGTH_SHORT);
            }

        }

    }

    private void getQrCodeInfo() {
        IWalletApiService apiService = new WalletApiService
                .WalletApiBuilder(getApplicationContext())
                .build();

        QRCodeInfo qrCodeInfo = null;
        try {
            qrCodeInfo = apiService.getQRCodeInfo(this.qrCode);

            QrCodeUtils.log(qrCodeInfo);

            Bundle bundle = new Bundle();

            bundle.putString("payloadFormatIndicator", qrCodeInfo.getPayloadFormatIndicator());
            bundle.putString("pointOfInitiationMethod", qrCodeInfo.getPointOfInitiationMethod());
            bundle.putString("globallyUniqueIdentifier", qrCodeInfo.getMerchantAccountInformation().getGloballyUniqueIdentifier());
            bundle.putString("merchantAccountInformation", qrCodeInfo.getMerchantAccountInformation().getMerchantAccountInformation());
            bundle.putString("logicNumber", qrCodeInfo.getMerchantAccountInformation().getLogicNumber());
            bundle.putString("merchantCategoryCode", qrCodeInfo.getMerchantCategoryCode());
            bundle.putString("transactionCurrency", qrCodeInfo.getTransactionCurrency());
            bundle.putString("transactionAmount", qrCodeInfo.getTransactionAmount());
            bundle.putString("countryCode", qrCodeInfo.getCountryCode());
            bundle.putString("merchantName", qrCodeInfo.getMerchantName());
            bundle.putString("merchantCity", qrCodeInfo.getMerchantCity());
            bundle.putString("transactionGloballyUniqueIdentifier", qrCodeInfo.getTransactionInformations().getTransactionGloballyUniqueIdentifier());
            bundle.putString("transactionId", qrCodeInfo.getTransactionInformations().getTransactionID());
            bundle.putString("transactionDate", qrCodeInfo.getTransactionInformations().getTransactionDate());
            bundle.putString("mainProduct", qrCodeInfo.getTransactionInformations().getMainProduct());
            bundle.putString("subProduct", qrCodeInfo.getTransactionInformations().getSubProduct());
            bundle.putString("paymentInstallments", qrCodeInfo.getTransactionInformations().getPaymentInstallments());
            bundle.putString("transactionType", qrCodeInfo.getTransactionInformations().getTransactionType());
            bundle.putString("merchantTransactionId", qrCodeInfo.getTransactionInformations().getMerchantTransactionID());
            bundle.putString("crc", qrCodeInfo.getCrc());

            Intent intent = new Intent(getApplicationContext(), QrCodeInfoActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),  "QR code inválido", Toast.LENGTH_SHORT).show();
            Log.e(Constants.TAG, e.getMessage(), e);
        }
    }
}
