package com.phoebus.appdemowallet.activities.payments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.phoebus.appdemowallet.R;

public class QrCodeInfoActivity extends AppCompatActivity {

    private EditText payloadFormatIndicator;
    private EditText pointOfInitiationMethod;
    private EditText globallyUniqueIdentifier;
    private EditText merchantAccountInformation;
    private EditText logicNumber;
    private EditText merchantCategoryCode;
    private EditText transactionCurrency;
    private EditText transactionAmount;
    private EditText countryCode;
    private EditText merchantName;
    private EditText merchantCity;
    private EditText transactionGloballyUniqueIdentifier;
    private EditText transactionId;
    private EditText transactionDate;
    private EditText mainProduct;
    private EditText subProduct;
    private EditText paymentInstallments;
    private EditText transactionType;
    private EditText crc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.infoQrCode);
        setContentView(R.layout.activity_qr_code_info);

        this.payloadFormatIndicator = this.findViewById(R.id.payloadFormatIndicator);
        this.pointOfInitiationMethod = this.findViewById(R.id.pointOfInitiationMethod);
        this.globallyUniqueIdentifier = this.findViewById(R.id.globallyUniqueIdentifier);
        this.merchantAccountInformation = this.findViewById(R.id.merchantAccountInformation);
        this.logicNumber = this.findViewById(R.id.logicNumber);
        this.merchantCategoryCode = this.findViewById(R.id.merchantCategoryCode);
        this.transactionCurrency = this.findViewById(R.id.transactionCurrency);
        this.transactionAmount = this.findViewById(R.id.transactionAmount);
        this.countryCode = this.findViewById(R.id.countryCode);
        this.merchantName = this.findViewById(R.id.merchantName);
        this.merchantCity = this.findViewById(R.id.merchantCity);
        this.transactionGloballyUniqueIdentifier = this.findViewById(R.id.transactionGloballyUniqueIdentifier);
        this.transactionId = this.findViewById(R.id.transactionId);
        this.transactionDate = this.findViewById(R.id.transactionDate);
        this.mainProduct = this.findViewById(R.id.mainProduct);
        this.subProduct = this.findViewById(R.id.subProduct);
        this.paymentInstallments = this.findViewById(R.id.paymentInstallments);
        this.transactionType = this.findViewById(R.id.transactionType);
        this.crc = this.findViewById(R.id.crc);

        setDefaultValues();
    }

    private void setDefaultValues() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.payloadFormatIndicator.setText(extras.getString("payloadFormatIndicator"));
            this.pointOfInitiationMethod.setText(extras.getString("pointOfInitiationMethod"));
            this.globallyUniqueIdentifier.setText(extras.getString("globallyUniqueIdentifier"));
            this.merchantAccountInformation.setText(extras.getString("merchantAccountInformation"));
            this.logicNumber.setText(extras.getString("logicNumber"));
            this.merchantCategoryCode.setText(extras.getString("merchantCategoryCode"));
            this.transactionCurrency.setText(extras.getString("transactionCurrency"));
            this.transactionAmount.setText(extras.getString("transactionAmount"));
            this.countryCode.setText(extras.getString("countryCode"));
            this.merchantName.setText(extras.getString("merchantName"));
            this.merchantCity.setText(extras.getString("merchantCity"));
            this.transactionGloballyUniqueIdentifier.setText(extras.getString("transactionGloballyUniqueIdentifier"));
            this.transactionId.setText(extras.getString("transactionId"));
            this.transactionDate.setText(extras.getString("transactionDate"));
            this.mainProduct.setText(extras.getString("mainProduct"));
            this.subProduct.setText(extras.getString("subProduct"));
            this.paymentInstallments.setText(extras.getString("paymentInstallments"));
            this.transactionType.setText(extras.getString("transactionType"));
            this.crc.setText(extras.getString("crc"));
        }
    }
}
