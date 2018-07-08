package com.example.akash.wiki.adapter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akash.wiki.R;
import com.example.akash.wiki.model.Page;
import com.example.akash.wiki.model.Terms;
import com.example.akash.wiki.model.Thumbnail;
import com.example.akash.wiki.ui.SearchFragment;
import com.example.akash.wiki.utils.PicassoCircularTransformation;
import com.example.akash.wiki.utils.Utils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class AutoCompleteTextviewAdapter extends ArrayAdapter<Page> implements Filterable{

    public static final String TAG= AutoCompleteTextviewAdapter.class.getSimpleName();
    public static final String WIKIPEDIA_API_BASE= "https://en.wikipedia.org/w";

    private Context mContext;
    private int mResource;
    private int mTextViewResourceId;
    private List<Page> pageResultList= new ArrayList<>();
    private SearchFragment mSearchFragment;
    private String mConstraint;

    public AutoCompleteTextviewAdapter(Context context, int textViewResourceId, SearchFragment fragment) {
        super(context, textViewResourceId);
        this.mContext = context;
        this.mTextViewResourceId = textViewResourceId;
        this.mSearchFragment= fragment;
    }

    @Override
    public Page getItem(int position) {
        return pageResultList.get(position);
    }

    @Override
    public int getCount() {
        return pageResultList == null ? 0: pageResultList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create a ViewHolder reference
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater layoutInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_item_search, null);
            holder.name = (TextView) convertView.findViewById(R.id.tvName);
            holder.description= (TextView) convertView.findViewById(R.id.tvDescription);
            holder.icon= (ImageView) convertView.findViewById(R.id.ivSearchIcon);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Page page= null;
        if(pageResultList != null && pageResultList.size() > 0)
            page= pageResultList.get(position);

        if(page != null) {
            holder.name.setText(page.getTitle());

            Terms terms = page.getTerms();
            List<String> description = null;
            if (terms != null) {
                description = terms.getDescription();
            }
            if (description != null && description.size() > 0) {
                holder.description.setText(description.get(0));
            }

            Thumbnail thumbnail = page.getThumbnail();
            String urlString = null;
            if (thumbnail != null) {
                urlString = thumbnail.getSource();
            }

            loadImageWithPicasso(urlString, holder);
        }

        convertView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    if(inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS)){
                        return true;
                    }
                }

                return false;
            }
        });


        return convertView;
    }

    private void loadImageWithPicasso(String thumbnailUrl, ViewHolder holder) {
        if (thumbnailUrl != null) {
            Picasso.with(mContext).load(thumbnailUrl)
                    .transform(new PicassoCircularTransformation())
                    .into(holder.icon);
        } else {
            Picasso.with(mContext).load(R.drawable.no_thumbnail)
                    .transform(new PicassoCircularTransformation())
                    .into(holder.icon);
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    mConstraint= constraint.toString();
                    pageResultList.clear();
                    Handler handler= new Handler(Looper.getMainLooper());
                    if(Utils.isNetworkAvailable(mContext))
                        handler.post(() -> mSearchFragment.showProgressBar(true));
                    else
                        handler.post(() -> Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show());
                    // Retrieve the autocomplete results. Also, runs on a worker thread
                    pageResultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = pageResultList;
                    filterResults.count = pageResultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mSearchFragment.showProgressBar(false);
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }};
    }



    private List<Page> autocomplete(String input) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(WIKIPEDIA_API_BASE + "/api.php?");
            sb.append("action=query");
            sb.append("&format=json");
            sb.append("&prop=pageimages");
            sb.append("%7Cpageterms");
            sb.append("&generator=prefixsearch");
            sb.append("&redirects=1");
            sb.append("&formatversion=2");
            sb.append("&piprop=thumbnail");
            sb.append("&pithumbsize=50");
            sb.append("&pilimit=50");
            sb.append("&wbptterms=description");
            //sb.append("&gpssearch=Albert%20Ei");
            sb.append("&gpssearch=" + URLEncoder.encode(input, "utf8"));
            sb.append("&gpslimit=50");

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
            return pageResultList;
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Wiki API", e);
            return pageResultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        Gson gson= new Gson();

        try {
            JSONObject jsonObject = new JSONObject(jsonResults.toString());
            JSONObject queryObject= jsonObject.getJSONObject("query");

            JSONArray pagesArray = queryObject.getJSONArray("pages");

            for (int i = 0; i < pagesArray.length(); i++) {
                JSONObject pages = pagesArray.getJSONObject(i);

                Long pageId= pages.getLong("pageid");
                String title= pages.getString("title");


                Thumbnail thumbnail= null;
                if(pages.has("thumbnail")) {
                    JSONObject thumbnailJSONObject = pages.getJSONObject("thumbnail");
                    thumbnail = gson.fromJson(thumbnailJSONObject.toString(), Thumbnail.class);
                }

                Terms terms= null;
                if(pages.has("terms")) {
                    JSONObject termsJSONObject = pages.getJSONObject("terms");
                    terms = gson.fromJson(termsJSONObject.toString(), Terms.class);
                }

                pageResultList.add(new Page(String.valueOf(pageId), title, thumbnail, terms));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return pageResultList;
    }


    public List<Page> getPageResultList() {
        return pageResultList;
    }

    public void setPageResultList(List<Page> pageResultList) {
        this.pageResultList = pageResultList;
    }

    public String getmConstraint() {
        return mConstraint;
    }

    public void setmConstraint(String mConstraint) {
        this.mConstraint = mConstraint;
    }
}
