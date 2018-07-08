package com.example.akash.wiki.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.akash.wiki.R;
import com.example.akash.wiki.database.SearchEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.RecentSearchViewHolder> {

    private List<SearchEntry> mSearchList;
    private Context mContext;
    private ClickHandler mClickHandler;

    public interface ClickHandler{
        void itemClicked(String searchStr);
    }

    public RecentSearchAdapter(List<SearchEntry> mSearchList, Context mContext, ClickHandler clickHandler) {
        this.mSearchList = mSearchList;
        this.mContext = mContext;
        this.mClickHandler= clickHandler;
    }

    @NonNull
    @Override
    public RecentSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_recent_search, parent, false);
        return new RecentSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentSearchViewHolder holder, int position) {
        holder.textView.setText(mSearchList.get(position).getKeyword());
    }

    @Override
    public int getItemCount() {
        return mSearchList == null ? 0 : mSearchList.size();
    }

    public void setSearches(List<SearchEntry> searchEntryList){
        mSearchList= searchEntryList;
        notifyDataSetChanged();
    }

    class RecentSearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView)
        TextView textView;

        public RecentSearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.itemClicked(mSearchList.get(getAdapterPosition()).getKeyword());
                }
            });
        }
    }
}
