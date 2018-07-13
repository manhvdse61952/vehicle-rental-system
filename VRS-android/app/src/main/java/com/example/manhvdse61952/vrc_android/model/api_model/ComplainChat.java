package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class ComplainChat implements Serializable {
    private int contractID;
    private int senderID;
    private String chatContent;
    private long chatTime;

    public ComplainChat(int contractID, int senderID, String chatContent, long chatTime) {
        this.contractID = contractID;
        this.senderID = senderID;
        this.chatContent = chatContent;
        this.chatTime = chatTime;
    }

    public ComplainChat() {
    }

    public int getContractID() {
        return contractID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public long getChatTime() {
        return chatTime;
    }

    public void setChatTime(long chatTime) {
        this.chatTime = chatTime;
    }
}
