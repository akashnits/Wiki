package com.example.akash.wiki.ui;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.akash.wiki.R;
import com.example.akash.wiki.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageDetailsFragment extends Fragment {

    public static final String TAG = PageDetailsFragment.class.getSimpleName();

    @BindView(R.id.toolbarDetails)
    Toolbar toolbarDetails;
    Unbinder unbinder;
    @BindView(R.id.wvDetails)
    WebView wvDetails;
    @BindView(R.id.cvDetails)
    CardView cvDetails;

    private static final int LOADER_ID = 0;
    @BindView(R.id.pbDetails)
    ProgressBar pbDetails;


    private Context mContext;
    private String mPageTitle;
    private String mResult;

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

        if (getArguments() != null) {
            mPageTitle = getArguments().getString("title");
        }
        /*LoaderManager loaderManager= getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);*/
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
        mContext = context;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbarDetails.setTitle(getResources().getString(R.string.details));
        toolbarDetails.setTitleTextColor(Color.WHITE);
        pbDetails.setVisibility(View.VISIBLE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarDetails);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //caching mechanism for page loaded in webview

        wvDetails.getSettings().setJavaScriptEnabled(true);
        wvDetails.getSettings().setAppCacheMaxSize( 5 * 1024 * 1024 ); // 5MB
        wvDetails.getSettings().setAppCachePath(mContext.getCacheDir().getAbsolutePath() );
        wvDetails.getSettings().setAllowFileAccess( true );
        wvDetails.getSettings().setAppCacheEnabled( true );
        wvDetails.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        if ( !Utils.isNetworkAvailable(mContext)) {
            wvDetails.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        }

        wvDetails.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbDetails.setVisibility(View.INVISIBLE);
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getContext(), description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
            }
        });
        mPageTitle.replace("\\s+", "_");
        wvDetails.loadUrl("https://en.wikipedia.org/wiki/" + mPageTitle);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
