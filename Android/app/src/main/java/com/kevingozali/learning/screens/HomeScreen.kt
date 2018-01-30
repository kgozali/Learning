package com.kevingozali.learning.screens

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.Toolbar
import com.bumptech.glide.load.model.GlideUrl
import com.kevingozali.learning.R
import com.kevingozali.learning.fastadapter.AdapterExt
import com.kevingozali.learning.fastadapter.ViewItem
import com.kevingozali.learning.molecule.AtomicImagesWithLabel
import com.kevingozali.learning.utils.BaseState
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.activity_home_screen.*


class HomeScreen : AppCompatActivity() {

    private val state: State = State()
    private val renderer = Renderer()
    private val adapterExt = AdapterExt()
    private lateinit var adapter: FastItemAdapter<ViewItem<*>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
    }

    override fun onStart() {
        super.onStart()
        setupView()
    }

    private fun setupView() {
        adapter = adapterExt.initializeAdapter(this, rvContent)
        renderer.render(this, state)
    }

    protected fun onClickListener() {
        val intent = Intent(this, TransactionScreen::class.java)
        startActivity(intent)
    }

    class State : BaseState() {

    }

    class Renderer {

        fun render(activity: HomeScreen, state: State) {
            renderBody(activity, state)
        }

        fun renderBody(activity: HomeScreen, state: State) {
            val listOfItems: MutableList<ViewItem<*>> = ArrayList()
            activity.tvBottom.apply {
                text = state.margin
                setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }

            listOfItems.add(AtomicImagesWithLabel.item {
                labelText = "asda"
                backgroundColor = R.color.colorAccent
                onClickListener = { activity.onClickListener() }
            })
            listOfItems.add(AtomicImagesWithLabel.item {
                labelText = "hfjasfhlkashflasfhlasfaslflasafda"
                backgroundColor = R.color.colorAccent
            }
            )
            activity.adapter.set(listOfItems)
        }
    }

    companion object {
    }

}
