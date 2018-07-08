package com.example.akash.wiki.ui;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.akash.wiki.R;
import com.example.akash.wiki.adapter.PagesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageDetailsFragment extends Fragment {

    public static final String BASE_PAGE_API=
            "https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=jsonfm&formatversion=2";

    @BindView(R.id.toolbarDetails)
    Toolbar toolbarDetails;
    Unbinder unbinder;
    @BindView(R.id.wvDetails)
    WebView wvDetails;
    @BindView(R.id.cvDetails)
    CardView cvDetails;

    private Context mContext;
    private String mPageId;

    public PageDetailsFragment() {
        // Required empty public constructor
    }

    public static PageDetailsFragment newInstance(Bundle args) {
        PageDetailsFragment fragment = new PageDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mPageId= getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext= context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbarDetails.setTitle(getResources().getString(R.string.details));
        toolbarDetails.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarDetails);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wvDetails.getSettings().setJavaScriptEnabled(true);
        wvDetails.setWebViewClient(new WebViewClient(){
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getContext(), description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
            }
        });
        wvDetails.loadUrl(BASE_PAGE_API + "&pageids=" + mPageId);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
