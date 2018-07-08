package com.example.akash.wiki;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.akash.wiki.model.Page;

import java.util.List;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private List<Page> pageResultList;

    public ViewModelFactory() {
    }

    public ViewModelFactory(List<Page> pageResultList) {
        this.pageResultList = pageResultList;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(pageResultList);
    }

    public List<Page> getPageResultList() {
        return pageResultList;
    }

    public void setPageResultList(List<Page> pageResultList) {
        this.pageResultList = pageResultList;
    }
}

