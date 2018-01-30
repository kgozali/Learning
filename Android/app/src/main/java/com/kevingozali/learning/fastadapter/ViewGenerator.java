package com.kevingozali.learning.fastadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kevingozali on 01/30/18.
 */

public interface ViewGenerator<V extends View> {
    V generateView(Context ctx, ViewGroup parent);
}
