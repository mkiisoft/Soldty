package com.renderas.soldty.utils.paginglistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.renderas.soldty.R;


public class LoadingView extends LinearLayout {

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.loading_view, this);
    }


}
