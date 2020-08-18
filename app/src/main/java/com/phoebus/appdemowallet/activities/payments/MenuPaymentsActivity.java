package com.phoebus.appdemowallet.activities.payments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.phoebus.appdemowallet.R;
import com.phoebus.appdemowallet.adapter.AdapterCustomIcon;

import java.util.ArrayList;

public class MenuPaymentsActivity extends AppCompatActivity {

    private final int MENU_START_PAYMENT = 0;
    private final int MENU_CONSULT_PAYMENT = 1;
    private final int MENU_QR_CODE_INFO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_payments);
        this.setTitle(R.string.payment);

        ListView listMenu = findViewById(R.id.lvMenu);
        ArrayList<String> options = getMenuOptions();
        AdapterCustomIcon adapter = new AdapterCustomIcon(options, this);

        listMenu.setAdapter(adapter);
        listMenu.setOnItemClickListener((adapterView, view, position, id) -> {

            switch (position) {
                case MENU_START_PAYMENT:
                    Intent paymentIntent = new Intent(this, PaymentActivity.class);
                    startActivity(paymentIntent);
                    break;
                case MENU_CONSULT_PAYMENT:
                    Intent consultPaymentIntent = new Intent(this, ConsultPaymentsActivity.class);
                    startActivity(consultPaymentIntent);
                    break;
                case MENU_QR_CODE_INFO:
                    Intent readQrCodeIntent = new Intent(this, ReadQrCodeActivity.class);
                    startActivity(readQrCodeIntent);
                    break;
            }
        });
    }

    private ArrayList<String> getMenuOptions() {
        ArrayList<String> list = new ArrayList<>();

        list.add(getString(R.string.startPayment));
        list.add(getString(R.string.consultPayments));
        list.add(getString(R.string.readInformationCapturedWithQrCode));

        return list;
    }
}
