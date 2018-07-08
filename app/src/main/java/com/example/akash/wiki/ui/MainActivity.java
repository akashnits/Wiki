package com.example.akash.wiki.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.akash.wiki.R;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        if(savedInstanceState == null) {
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance(), "searchFragment")
                    .commit();
        }
    }


    public void commitPageDetailsFragment(Bundle args){
        mFragmentManager
                .beginTransaction()
                .add(R.id.container, PageDetailsFragment.newInstance(args), "pageDetailsFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        mFragmentManager.popBackStack();
        return true;
    }
}
