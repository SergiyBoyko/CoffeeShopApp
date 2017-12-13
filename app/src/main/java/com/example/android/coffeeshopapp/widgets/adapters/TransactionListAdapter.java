package com.example.android.coffeeshopapp.widgets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.coffeeshopapp.R;
import com.example.android.coffeeshopapp.common.Constants;
import com.example.android.coffeeshopapp.model.entities.PurchaseTransactionEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {

    private Context context;
    private List<PurchaseTransactionEntity> transactionList = null;
    private OnRefundClickListener refundClickListener;

    public TransactionListAdapter(Context context, List<PurchaseTransactionEntity> transactionList,
                                  OnRefundClickListener refundClickListener) {
        this.context = context;
        this.transactionList = transactionList;
        this.refundClickListener = refundClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        long ms = transactionList.get(position).getDate();
        Date date = new Date(ms);
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH);
        holder.date.setText(dateFormat.format(date));
        holder.cardId.setText(String.valueOf(transactionList.get(position).getCardId()));
        holder.amount.setText(String.format(Locale.ENGLISH, "%.2f", transactionList.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        if (transactionList == null) return 0;
        return transactionList.size();
    }

    public void setTransactionList(List<PurchaseTransactionEntity> transactionList) {
        this.transactionList = transactionList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_id_num)
        TextView cardId;
        @BindView(R.id.transaction_date)
        TextView date;
        @BindView(R.id.amount_field)
        TextView amount;
        @BindView(R.id.refund_but)
        Button refundButton;


        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_transaction, parent, false));

            ButterKnife.bind(this, itemView);

            refundButton.setOnClickListener(view ->
                    refundClickListener.onRefundClicked(transactionList.get(getAdapterPosition()).getCardId(),
                            transactionList.get(getAdapterPosition()).getId()));
        }


    }

    public interface OnRefundClickListener {

        void onRefundClicked(long cardId, long purchaseId);

    }

}
