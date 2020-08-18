package com.phoebus.appdemowallet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.phoebus.appdemowallet.R;

public class ResponseActivity extends AppCompatActivity {

    private EditText edtContent;

    private Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Response");

        setContentView(R.layout.activity_response);

        edtContent = findViewById(R.id.content);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> submit(v));

        Intent it = getIntent();
        if (it != null)
        {
            edtContent.setText(it.getStringExtra("result"));
        }

    }

    public void submit(View view){
        finish();
    }


}
