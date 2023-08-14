package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

public class Transaction {
    private int transactionId;
    private String transactionType;
    private int senderAccountId;
    private int receiverAccountId;
    private String status;
    private BigDecimal transferAmount;
    private String messageText;

    public Transaction(String transactionType, int senderAccountId, int receiverAccountId, String status, BigDecimal transferAmount, String messageText) {
        this.transactionType = transactionType;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.status = status;
        this.transferAmount = transferAmount;
        this.messageText = messageText;
    }

    public Transaction(int transactionId, String transactionType, int senderAccountId, int receiverAccountId, String status, BigDecimal transferAmount, String messageText) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.status = status;
        this.transferAmount = transferAmount;
        this.messageText = messageText;
    }

    public Transaction() {
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public int getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(int receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", transactionType='" + transactionType + '\'' +
                ", senderAccountId=" + senderAccountId +
                ", receiverAccountId=" + receiverAccountId +
                ", status=" + status +
                ", transferAmount=" + transferAmount +
                ", messageText='" + messageText + '\'' +
                '}';
    }
}
