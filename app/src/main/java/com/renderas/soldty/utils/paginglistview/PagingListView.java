package com.renderas.soldty.utils.paginglistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.renderas.soldty.R;

import java.util.List;


public class PagingListView extends ListView {

    private boolean isLoading;
    private boolean hasMoreItems;
    private Pagingable pagingableListener;
    private LoadingView loadingView;
    private OnScrollListener onScrollListener;
//    private com.renderas.soldty.utils.paginglistview.LoadingView loadingView;

    public PagingListView(Context context) {
        super(context);
        init();
    }

    public PagingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PagingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setPagingableListener(Pagingable pagingableListener) {
        this.pagingableListener = pagingableListener;
    }

    public void setHasMoreItems(boolean hasMoreItems) {
        this.hasMoreItems = hasMoreItems;
        if (!this.hasMoreItems) {
            removeFooterView(loadingView);
        } else if (findViewById(R.id.loading_view) == null) {
            addFooterView(loadingView);
            ListAdapter adapter = ((HeaderViewListAdapter) getAdapter()).getWrappedAdapter();
            setAdapter(adapter);
        }
    }

    public void removeFooterView(boolean footer) {
        boolean remove = footer;
        if (loadingView != null && !remove) {
            removeFooterView(loadingView);
            loadingView = new com.renderas.soldty.utils.paginglistview.LoadingView(getContext());
            addFooterView(loadingView);
        } else if (loadingView != null && remove) {
            removeFooterView(loadingView);
        }
    }

    public boolean hasMoreItems() {
        return this.hasMoreItems;
    }

    public void onFinishLoading(boolean hasMoreItems, List<? extends Object> newItems) {
        setHasMoreItems(hasMoreItems);
        setIsLoading(false);
        if (newItems != null && newItems.size() > 0) {
            ListAdapter adapter = ((HeaderViewListAdapter) getAdapter()).getWrappedAdapter();
            if (adapter instanceof PagingBaseAdapter) {
                ((PagingBaseAdapter) adapter).addMoreItems(newItems);
            }
        }
    }

    private void init() {
        isLoading = false;
        loadingView = new LoadingView(getContext());
        addFooterView(loadingView);
        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Dispatch to child OnScrollListener
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //Dispatch to child OnScrollListener
                if (onScrollListener != null) {
                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }

                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (!isLoading && hasMoreItems && (lastVisibleItem == totalItemCount)) {
                    if (pagingableListener != null) {
                        isLoading = true;
                        pagingableListener.onLoadMoreItems();
                    }

                }
            }
        });
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        onScrollListener = listener;
    }

    public interface Pagingable {
        void onLoadMoreItems();
    }
}
