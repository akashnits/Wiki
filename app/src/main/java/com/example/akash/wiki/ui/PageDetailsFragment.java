package com.example.akash.wiki.ui;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{

    public static final String BASE_PAGE_API=
            "https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=jsonfm&formatversion=2";
    public static final String TAG= PageDetailsFragment.class.getSimpleName();

    @BindView(R.id.toolbarDetails)
    Toolbar toolbarDetails;
    Unbinder unbinder;
    @BindView(R.id.wvDetails)
    WebView wvDetails;
    @BindView(R.id.cvDetails)
    CardView cvDetails;

    private static final int LOADER_ID = 0;


    private Context mContext;
    private String mPageId;
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

        if(getArguments() != null) {
            mPageId= getArguments().getString("id");
        }
        LoaderManager loaderManager= getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
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

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<String>(mContext) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(mResult == null){
                    forceLoad();
                }
                else{
                    deliverResult(mResult);
                }
            }

            @Nullable
            @Override
            public String loadInBackground() {

                //TODO: Refactor code for url connection
                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                try {
                    StringBuilder sb = new StringBuilder(BASE_PAGE_API);
                    sb.append("&pageids=" + mPageId);

                    Log.v(TAG, sb.toString());

                    URL url = new URL(sb.toString());
                    conn = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    // Load the results into a StringBuilder
                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        jsonResults.append(buff, 0, read);
                    }
                } catch (MalformedURLException e) {
                    Log.e(TAG, "Error processing Wiki API URL", e);
                } catch (IOException e) {
                    Log.e(TAG, "Error connecting to Wiki API", e);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
                return jsonResults.toString();
            }

            @Override
            public void deliverResult(@Nullable String data) {
                mResult= data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        wvDetails.getSettings().setJavaScriptEnabled(true);
        wvDetails.loadData(mResult, "text/html", "UTF-8");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbarDetails.setTitle(getResources().getString(R.string.details));
        toolbarDetails.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarDetails);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*wvDetails.getSettings().setJavaScriptEnabled(true);
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
        wvDetails.loadUrl(BASE_PAGE_API + "&pageids=" + mPageId);*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
