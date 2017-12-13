package com.example.android.coffeeshopapp.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dev_serhii on 13.12.2017.
 */

public class PurchaseTransactionEntity {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("card_id")
    @Expose
    private Long cardId;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("date")
    @Expose
    private Long date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
