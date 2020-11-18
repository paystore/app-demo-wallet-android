package com.phoebus.appdemowallet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phoebus.appdemowallet.R;
import com.phoebus.libwallet.models.GetPaymentResponse;

import java.util.List;

public class ListPaymentsAdapter extends RecyclerView.Adapter<ListPaymentsAdapter.ListPaymentViewHolder> {
    private List<GetPaymentResponse> content;

    public ListPaymentsAdapter(List<GetPaymentResponse> content) {
        this.content = content;
    }

    @NonNull
    @Override
    public ListPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_view_holder, parent, false);
        ListPaymentViewHolder vh = new ListPaymentViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListPaymentViewHolder holder, int position) {
        holder.appTransactionId.setText(content.get(position).getAppTransactionId());
        holder.paymentId.setText(content.get(position).getPaymentId());
        holder.status.setText(content.get(position).getStatus());
        holder.paymentDate.setText(content.get(position).getPaymentDateTime());
        holder.authorizationCode.setText(content.get(position).getAuthorizationCode());
        holder.cardId.setText(content.get(position).getCardId());
        holder.amount.setText(content.get(position).getAmount().toString());
        holder.product.setText(content.get(position).getProduct().toString());
        holder.productType.setText(content.get(position).getProductType().toString());
        holder.installments.setText(content.get(position).getInstallments().toString());
        holder.merchantName.setText(content.get(position).getMerchantName());
        holder.merchantCity.setText(content.get(position).getMerchantCity());
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    class ListPaymentViewHolder extends RecyclerView.ViewHolder {
        public TextView appTransactionId;
        public TextView paymentId;
        public TextView status;
        public TextView paymentDate;
        public TextView authorizationCode;
        public TextView cardId;
        public TextView amount;
        public TextView product;
        public TextView productType;
        public TextView installments;
        public TextView merchantName;
        public TextView merchantCity;

        public ListPaymentViewHolder(View view) {
            super(view);
            this.appTransactionId = view.findViewById(R.id.app_transaction_id_list);
            this.paymentId = view.findViewById(R.id.paymentId);
            this.status = view.findViewById(R.id.status);
            this.paymentDate = view.findViewById(R.id.paymentDateTime);
            this.authorizationCode = view.findViewById(R.id.autorizationCode);
            this.cardId = view.findViewById(R.id.cardId);
            this.amount = view.findViewById(R.id.amount);
            this.product = view.findViewById(R.id.product);
            this.productType = view.findViewById(R.id.productType);
            this.installments = view.findViewById(R.id.installments);
            this.merchantName = view.findViewById(R.id.merchantName);
            this.merchantCity = view.findViewById(R.id.merchantCity);
        }
    }
}
