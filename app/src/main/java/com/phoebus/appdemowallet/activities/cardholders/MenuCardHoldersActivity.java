package com.phoebus.appdemowallet.activities.cardholders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.phoebus.appdemowallet.R;
import com.phoebus.appdemowallet.adapter.AdapterCustomIcon;

import java.util.ArrayList;

public class MenuCardHoldersActivity extends AppCompatActivity {

    private final int MENU_CREATE_CARD_HOLDER = 0;
    private final int MENU_CONFIRM_CARD_HOLDER = 1;
    private final int MENU_REVALIDATE_CARD_HOLDER = 2;
    private final int MENU_REMOVE_CARD_HOLDER = 3;
    private final int MENU_RESEND_CONFIRMATION_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_card_holders);
        this.setTitle(R.string.card_holders);

        ListView listMenu = findViewById(R.id.lvMenu);
        ArrayList<String> options = getMenuOptions();
        AdapterCustomIcon adapter = new AdapterCustomIcon(options, this);

        listMenu.setAdapter(adapter);
        listMenu.setOnItemClickListener((adapterView, view, position, id) -> {

            switch (position) {
                case MENU_CREATE_CARD_HOLDER:
                    Intent createCardHolderIntent = new Intent(this, CreateCardHolderActivity.class);
                    startActivity(createCardHolderIntent);
                    break;
                case MENU_CONFIRM_CARD_HOLDER:
                    Intent confirmCardholderIntent = new Intent(this, ConfirmCardHolderActivity.class);
                    startActivity(confirmCardholderIntent);
                    break;
                case MENU_REVALIDATE_CARD_HOLDER:
                    Intent revalidateCardHolderIntent = new Intent(this, RevalidateCardHolderActivity.class);
                    startActivity(revalidateCardHolderIntent);
                    break;
                case MENU_REMOVE_CARD_HOLDER:
                    Intent removeCardHolderIntent = new Intent(this, RemoveCardHolderActivity.class);
                    startActivity(removeCardHolderIntent);
                    break;
                case MENU_RESEND_CONFIRMATION_CODE:
                    Intent resendConfirmationCodeIntent = new Intent(this, ResendConfirmationCodeActivity.class);
                    startActivity(resendConfirmationCodeIntent);
                    break;
            }
        });

    }

    private ArrayList<String> getMenuOptions() {
        ArrayList<String> list = new ArrayList<>();

        list.add(getString(R.string.createCardHolder));
        list.add(getString(R.string.confirmationCardHolder));
        list.add(getString(R.string.revalidateCardHolder));
        list.add(getString(R.string.removeCardHolder));
        list.add(getString(R.string.resendConfirmationCode));

        return list;
    }
}
