package com.kevingozali.learning.fastadapter;

import android.view.View;

/**
 * Created by kevingozali on 01/30/18.
 */

public interface ViewBinder<V extends View> {
    void bindView(V view, ViewItem<V> item);
}
