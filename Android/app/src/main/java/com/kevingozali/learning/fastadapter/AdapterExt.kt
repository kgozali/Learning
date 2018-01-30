package com.kevingozali.learning.fastadapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter

/**
 * Created by gozali on 30/01/18.
 */
class AdapterExt {
    fun setupAdapter(orientation: Int, rv: RecyclerView, context: Context)
            : FastItemAdapter<ViewItem<*>> {
        val adapter: FastItemAdapter<ViewItem<*>>
        if (rv.adapter == null) {
            adapter = FastItemAdapter()
            rv.itemAnimator = null
            if (rv.layoutManager == null) {
                val llm = LinearLayoutManager(context)
                llm.orientation = orientation
                rv.layoutManager = llm
            }

            rv.adapter = adapter
        } else {
            adapter = rv.adapter as FastItemAdapter<ViewItem<*>>
        }
        return adapter
    }
}