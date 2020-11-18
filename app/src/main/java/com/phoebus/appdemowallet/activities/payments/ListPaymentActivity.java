package com.phoebus.appdemowallet.activities.payments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.phoebus.appdemowallet.R;
import com.phoebus.appdemowallet.adapter.ListPaymentsAdapter;
import com.phoebus.libwallet.models.GetPaymentResponse;
import com.phoebus.libwallet.models.GetPaymentResponsePage;

import java.util.List;

public class ListPaymentActivity extends AppCompatActivity {
    private List<GetPaymentResponse> content;
    private RecyclerView paymentRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_payments);
        paymentRecyclerView = this.findViewById(R.id.payment_list);
        Gson gson = new Gson();
        Intent it = getIntent();
        GetPaymentResponsePage response = gson.fromJson(it.getStringExtra("result"), GetPaymentResponsePage.class);
        content = response.getContent();
        paymentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentRecyclerView.setAdapter(new ListPaymentsAdapter(content));
    }
}