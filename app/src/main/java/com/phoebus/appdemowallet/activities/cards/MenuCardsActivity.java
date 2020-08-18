package com.phoebus.appdemowallet.activities.cards;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.phoebus.appdemowallet.R;
import com.phoebus.appdemowallet.adapter.AdapterCustomIcon;

import java.util.ArrayList;

public class MenuCardsActivity extends AppCompatActivity {

    private final int MENU_SAVE_CARD = 0;
    private final int MENU_ACTIVATE_CARD = 1;
    private final int MENU_DEACTIVATE_CARD = 2;
    private final int MENU_PREFERRED_CARD = 3;
    private final int MENU_REMOVE_CARD = 4;
    private final int MENU_GET_CARD = 5;
    private final int MENU_GET_CARDS = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_cards);
        this.setTitle(R.string.cards);

        ListView listMenu = findViewById(R.id.lvMenu);
        ArrayList<String> options = getMenuOptions();
        AdapterCustomIcon adapter = new AdapterCustomIcon(options, this);

        listMenu.setAdapter(adapter);
        listMenu.setOnItemClickListener((adapterView, view, position, id) -> {

            switch (position) {
                case MENU_SAVE_CARD:
                    Intent saveCardIntent = new Intent(this, SaveCardActivity.class);
                    startActivity(saveCardIntent);
                    break;
                case MENU_REMOVE_CARD:
                    Intent removeCardIntent = new Intent(this, RemoveCardActivity.class);
                    startActivity(removeCardIntent);
                    break;
                case MENU_PREFERRED_CARD:
                    Intent preferredCardIntent = new Intent(this, CreatePreferredCardActivity.class);
                    startActivity(preferredCardIntent);
                    break;
                case MENU_ACTIVATE_CARD:
                    Intent activateCardIntent = new Intent(this, ActivateCardActivity.class);
                    startActivity(activateCardIntent);
                    break;
                case MENU_DEACTIVATE_CARD:
                    Intent deactivateCardIntent = new Intent(this, DeactivateCardActivity.class);
                    startActivity(deactivateCardIntent);
                    break;
                case MENU_GET_CARD:
                    Intent getCardIntent = new Intent(this, GetCardActivity.class);
                    startActivity(getCardIntent);
                    break;
                case MENU_GET_CARDS:
                    Intent getCardsIntent = new Intent(this, GetCardsActivity.class);
                    startActivity(getCardsIntent);
                    break;
            }
        });
    }

    private ArrayList<String> getMenuOptions() {
        ArrayList<String> list = new ArrayList<>();

        list.add(getString(R.string.saveCard));
        list.add(getString(R.string.activateCard));
        list.add(getString(R.string.deactivateCard));
        list.add(getString(R.string.defineFavoriteCard));
        list.add(getString(R.string.removeCard));
        list.add(getString(R.string.getCard));
        list.add(getString(R.string.getCards));

        return list;
    }
}
