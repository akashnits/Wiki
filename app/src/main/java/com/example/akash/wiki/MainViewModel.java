package com.example.akash.wiki;

import android.arch.lifecycle.ViewModel;

import com.example.akash.wiki.model.Page;

import java.util.List;

public class MainViewModel extends ViewModel {

    private List<Page> pageResultList;

    public MainViewModel(List<Page> pageResultList) {
        this.pageResultList = pageResultList;
    }

    public List<Page> getPageResultList() {
        return pageResultList;
    }
}
