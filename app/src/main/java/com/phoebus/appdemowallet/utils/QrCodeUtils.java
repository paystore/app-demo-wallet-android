package com.phoebus.appdemowallet.utils;

import android.util.Log;

import com.phoebus.libwallet.models.qrcode.QRCodeInfo;
import com.phoebus.libwallet.utils.Constants;

public class QrCodeUtils {

    public static String sample() {
        return "00020101021226600032621e06636d0841909624c146d5c0f6ef01080001120502080000000052040056530398654120000000044005802BR5918Testes - Automação6009Guarabira801540026http://localhost:3000/test0136c1766b1c-73f3-457b-baa8-4a1d3a56890a021220102010521703041000040400010502010602010736c1191e3e-0f1d-11eb-adc1-0242ac12000263042257";
    }

    public static void log(QRCodeInfo qrCodeInfo) {

        Log.d(Constants.TAG, "Informações do QRCode: {"
        + "\n PayloadFormatIndicator: " + qrCodeInfo.getPayloadFormatIndicator()
        + "\n PointOfInitiationMethod: " + qrCodeInfo.getPointOfInitiationMethod()
        + "\n MerchantAccountInformation - GloballyUniqueIdentifier: " + qrCodeInfo.getMerchantAccountInformation().getGloballyUniqueIdentifier()
        + "\n MerchantAccountInformation - MerchantAccountInformation: " + qrCodeInfo.getMerchantAccountInformation().getMerchantAccountInformation()
        + "\n MerchantAccountInformation - LogicNumber: " + qrCodeInfo.getMerchantAccountInformation().getLogicNumber()
        + "\n MerchantCategoryCode: " + qrCodeInfo.getMerchantCategoryCode()
        + "\n TransactionCurrency: " + qrCodeInfo.getTransactionCurrency()
        + "\n TransactionAmount: " + qrCodeInfo.getTransactionAmount()
        + "\n CountryCode: " + qrCodeInfo.getCountryCode()
        + "\n MerchantName: " + qrCodeInfo.getMerchantName()
        + "\n MerchantCity: " + qrCodeInfo.getMerchantCity()
        + "\n TransactionInformations - TransactionGloballyUniqueIdentifier: " + qrCodeInfo.getTransactionInformations().getTransactionGloballyUniqueIdentifier()
        + "\n TransactionInformations - TransactionID: " + qrCodeInfo.getTransactionInformations().getTransactionID()
        + "\n TransactionInformations - TransactionDate: " + qrCodeInfo.getTransactionInformations().getTransactionDate()
        + "\n TransactionInformations - MainProduct: " + qrCodeInfo.getTransactionInformations().getMainProduct()
        + "\n TransactionInformations - SubProduct: " + qrCodeInfo.getTransactionInformations().getSubProduct()
        + "\n TransactionInformations - PaymentInstallments: " + qrCodeInfo.getTransactionInformations().getPaymentInstallments()
        + "\n TransactionInformations - TransactionType: " + qrCodeInfo.getTransactionInformations().getTransactionType()
        + "\n TransactionInformations - MerchantTransactionID: " + qrCodeInfo.getTransactionInformations().getMerchantTransactionID()
        + "\n Crc: " + qrCodeInfo.getCrc()
        + "\n}");

    }

}
