package com.example.akash.wiki.ui;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.akash.wiki.R;
import com.example.akash.wiki.adapter.AutoCompleteTextviewAdapter;
import com.example.akash.wiki.adapter.RecentSearchAdapter;
import com.example.akash.wiki.data.MainViewModel;
import com.example.akash.wiki.data.ViewModelFactory;
import com.example.akash.wiki.database.AppDatabase;
import com.example.akash.wiki.database.SearchEntry;
import com.example.akash.wiki.model.Page;
import com.example.akash.wiki.utils.CustomAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener, RecentSearchAdapter.ClickHandler {


    Unbinder unbinder;
    @BindView(R.id.toolbarMain)
    Toolbar toolbarMain;
    @BindView(R.id.autocompleteView)
    CustomAutoCompleteTextView autocompleteView;
    @BindView(R.id.pbSearchResults)
    ProgressBar pbSearchResults;
    @BindView(R.id.tvRecentSearches)
    TextView tvRecentSearches;
    @BindView(R.id.ivDelete)
    ImageView ivDelete;
    @BindView(R.id.rvRecentSearches)
    RecyclerView rvRecentSearches;
    @BindView(R.id.lvRecentSearches)
    LinearLayout lvRecentSearches;
    @BindView(R.id.til)
    TextInputLayout til;

    private Context mContext;
    private AutoCompleteTextviewAdapter mPagesAdapter;
    private ViewModelFactory mViewModelFactory;
    private MainViewModel mainViewModel;
    private AppDatabase mDb;
    private RecentSearchAdapter mRecentSearchAdapter;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mDb = AppDatabase.getsInstance(mContext);
        mRecentSearchAdapter = new RecentSearchAdapter(new ArrayList<>(), mContext, this);
        retrieveSearchResults();
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
        mPagesAdapter = new AutoCompleteTextviewAdapter(mContext, R.layout.list_item_search, this);

        autocompleteView.setAdapter(mPagesAdapter);
        autocompleteView.setOnItemClickListener(this);

        rvRecentSearches.setLayoutManager(new LinearLayoutManager(mContext));
        rvRecentSearches.setAdapter(mRecentSearchAdapter);

        updateUI();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mainViewModel != null) {
            mPagesAdapter.setPageResultList(mainViewModel.getPageResultList());
            mPagesAdapter.notifyDataSetChanged();
            autocompleteView.showDropDown();
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
        //save the search keyword in database

        SearchEntry searchEntry = new SearchEntry(mPagesAdapter.getmConstraint(), new Date());
        Thread t = new Thread(() -> mDb.searchDao().insertSearch(searchEntry));
        t.start();

        autocompleteView.setText("");
        Page page = (Page) parent.getItemAtPosition(position);

        Bundle b = new Bundle();
        b.putString("title", page.getTitle());
        ((MainActivity) getActivity()).commitPageDetailsFragment(b);
    }

    public void showProgressBar(boolean flag) {
        if (flag && pbSearchResults != null)
            pbSearchResults.setVisibility(View.VISIBLE);
        else {
            if (pbSearchResults != null)
                pbSearchResults.setVisibility(View.INVISIBLE);
        }
    }

    private void retrieveSearchResults() {
        LiveData<List<SearchEntry>> searches = mDb.searchDao().loadAllSearches();
        searches.observe(this, (searchEntryList -> mRecentSearchAdapter.setSearches(searchEntryList)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ivDelete)
    public void onViewClicked() {
        Thread thread= new Thread(() -> mDb.searchDao().deleteAllSearches());
        thread.start();
        /*lvRecentSearches.setVisibility(View.GONE);*/
    }

    @Override
    public void itemClicked(String searchStr) {
        autocompleteView.requestFocus();
        autocompleteView.setText(searchStr);
        autocompleteView.setSelection(autocompleteView.getText().length());
    }

    private void updateUI(){
        LiveData<List<SearchEntry>> searches = mDb.searchDao().loadAllSearches();
        searches.observe(this, (searchEntryList -> {
            if(searchEntryList != null && !searchEntryList.isEmpty())
                lvRecentSearches.setVisibility(View.VISIBLE);
            else
                lvRecentSearches.setVisibility(View.INVISIBLE);}));
    }
}
