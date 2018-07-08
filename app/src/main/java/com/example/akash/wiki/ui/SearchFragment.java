package com.example.akash.wiki.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.example.akash.wiki.R;
import com.example.akash.wiki.adapter.PagesAdapter;
import com.example.akash.wiki.data.MainViewModel;
import com.example.akash.wiki.data.ViewModelFactory;
import com.example.akash.wiki.model.Page;
import com.example.akash.wiki.utils.CustomAutoCompleteTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener {


    Unbinder unbinder;
    @BindView(R.id.toolbarMain)
    Toolbar toolbarMain;
    @BindView(R.id.autocompleteView)
    CustomAutoCompleteTextView autocompleteView;
    @BindView(R.id.pbSearchResults)
    ProgressBar pbSearchResults;

    private Context mContext;
    private PagesAdapter mPagesAdapter;
    private ViewModelFactory mViewModelFactory;
    private MainViewModel mainViewModel;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPagesAdapter = new PagesAdapter(mContext, R.layout.list_item_search, this);

        autocompleteView.setAdapter(mPagesAdapter);
        autocompleteView.setOnItemClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (mainViewModel != null) {
            mPagesAdapter.setPageResultList(mainViewModel.getPageResultList());
            mPagesAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        mViewModelFactory = new ViewModelFactory(mPagesAdapter.getPageResultList());
        mainViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        autocompleteView.setText("");
        Page page = (Page) parent.getItemAtPosition(position);

        Bundle b = new Bundle();
        b.putString("title", page.getTitle());
        ((MainActivity) getActivity()).commitPageDetailsFragment(b);
    }

    public void showProgressBar(boolean flag){
        if(flag)
            pbSearchResults.setVisibility(View.VISIBLE);
        else
            pbSearchResults.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
