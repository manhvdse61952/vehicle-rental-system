package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;
import java.util.Date;

public class ComplainChat implements Serializable {
    private String contractID;
    private int senderID;
    private String chatContent;
    private String chatTime;

    public ComplainChat() {
    }

    public ComplainChat(String contractID, int senderID, String chatContent, String chatTime) {
        this.contractID = contractID;
        this.senderID = senderID;
        this.chatContent = chatContent;
        this.chatTime = chatTime;
    }


    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
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

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }
}
