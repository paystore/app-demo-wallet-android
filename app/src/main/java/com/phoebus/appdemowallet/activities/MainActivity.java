package com.phoebus.appdemowallet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.phoebus.appdemowallet.R;
import com.phoebus.appdemowallet.activities.cardholders.MenuCardHoldersActivity;
import com.phoebus.appdemowallet.activities.cards.MenuCardsActivity;
import com.phoebus.appdemowallet.activities.payments.MenuPaymentsActivity;
import com.phoebus.appdemowallet.adapter.AdapterCustomIcon;
import com.phoebus.appdemowallet.security.WalletPermissions;
import com.phoebus.appdemowallet.utils.ConstantsApp;
import com.phoebus.libwallet.utils.Helper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int MENU_CARD_HOLDER = 0;
    private final int MENU_CARD = 1;
    private final int MENU_PAYMENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.setTitle("Demo Wallet");

        // call configuration if is empty.
        if (Helper.readPrefsString(this, ConstantsApp.BASE_URL_CONFIG, ConstantsApp.PREFS_CONFIG).isEmpty() ||
                Helper.readPrefsString(this, ConstantsApp.TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG).isEmpty() ||
                Helper.readPrefsString(this, ConstantsApp.NOTIFICATION_TOKEN_CONFIG, ConstantsApp.PREFS_CONFIG).isEmpty()) {

            startActivity(new Intent(this, ConfigRequestActivity.class));

        }


        ListView listMenu = findViewById(R.id.lvMenu);
        ArrayList<String> options = getMenuOptions();
        AdapterCustomIcon adapter = new AdapterCustomIcon(options, this);

        listMenu.setAdapter(adapter);
        listMenu.setOnItemClickListener((adapterView, view, position, id) -> {

            switch (position) {
                case MENU_CARD_HOLDER:
                    Intent menuCardHolderIntent = new Intent(this, MenuCardHoldersActivity.class);
                    startActivity(menuCardHolderIntent);
                    break;
                case MENU_CARD:
                    Intent menuCardsIntent = new Intent(this, MenuCardsActivity.class);
                    startActivity(menuCardsIntent);
                    break;
                case MENU_PAYMENT:
                    Intent menuPaymentsIntent = new Intent(this, MenuPaymentsActivity.class);
                    startActivity(menuPaymentsIntent);
                    break;
            }
        });

        WalletPermissions.getInstance(this).requiresPermission();
    }

    private ArrayList<String> getMenuOptions() {
        ArrayList<String> list = new ArrayList<>();

        list.add(getString(R.string.card_holders));
        list.add(getString(R.string.cards));
        list.add(getString(R.string.payment));

        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_options_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, ConfigRequestActivity.class);
        startActivity(intent);
        return true;
    }

}
