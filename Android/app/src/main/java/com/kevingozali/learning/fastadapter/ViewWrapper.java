package com.kevingozali.learning.fastadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kevingozali on 01/30/18.
 */
public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {

    public final V view;

    public ViewWrapper(View itemView) {
        super(itemView);
        view = (V) itemView;
    }
}
