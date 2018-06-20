package com.example.manhvdse61952.vrc_android.layout.main;

import ir.mirrajabi.searchdialog.core.Searchable;

public class SearchAddressModel implements Searchable {
    private String mTitle;


    @Override
    public String getTitle() {
        return mTitle;
    }

    public SearchAddressModel(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
