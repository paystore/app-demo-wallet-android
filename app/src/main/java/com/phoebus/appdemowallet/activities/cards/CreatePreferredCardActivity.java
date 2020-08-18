package com.phoebus.appdemowallet.activities.cards;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.phoebus.appdemowallet.R;
import com.phoebus.appdemowallet.utils.ConstantsApp;
import com.phoebus.libwallet.models.IWalletApiService;
import com.phoebus.libwallet.service.WalletApiService;
import com.phoebus.libwallet.utils.Constants;
import com.phoebus.libwallet.utils.Helper;


public class CreatePreferredCardActivity extends AppCompatActivity {
    private String cardId;

    private EditText edtCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.defineFavoriteCard);

        setContentView(R.layout.activity_create_preferred_card);
        this.edtCardId = this.findViewById(R.id.card_id);
        String lastCardId = Helper.readPrefsString(getApplicationContext(), ConstantsApp.CARD_ID, ConstantsApp.PREFS_CONFIG);
        this.edtCardId.setText(lastCardId);
    }

    public void submitCreatePreferredCard(View view){
        this.cardId = this.edtCardId.getText().toString();
        Log.d("FORM PreferredCard", this.cardId);

        createPreferredCardTest();
    }

    private void createPreferredCardTest() {
        IWalletApiService apiService = new WalletApiService
                .WalletApiBuilder(getApplicationContext())
                .build();

        boolean isSuccess = apiService.setPreferredCard(this.cardId);
        String msg = isSuccess ? "Cartão favorito definido com sucesso" : "Falha ao definir o cartão favorito";
        Toast.makeText(getApplicationContext(),  msg, Toast.LENGTH_SHORT).show();
        Log.d(Constants.TAG, msg);
    }
}
