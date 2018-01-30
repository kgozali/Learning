package com.kevingozali.learning.screens

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.OrientationHelper
import android.widget.Toast
import com.kevingozali.learning.R
import com.kevingozali.learning.fastadapter.AdapterExt
import com.kevingozali.learning.fastadapter.ViewItem
import com.kevingozali.learning.molecule.AtomicImagesWithLabel
import com.kevingozali.learning.utils.BaseState
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.activity_home_screen.*

/**
 * Created by gozali on 30/01/18.
 */
class TransactionScreen : AppCompatActivity() {

    private val state: State = State()
    private val renderer = Renderer()
    private val adapterExt = AdapterExt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
    }

    override fun onStart() {
        super.onStart()
        setupView()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this,"Tes",Toast.LENGTH_LONG).show()
//        val alertDialog = AlertDialog.Builder(this@TransactionScreen).create()
//        alertDialog.setTitle("Back Pressed")
//        alertDialog.setMessage("Are you sure?")
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", {
//            dialogInterface, i ->
//            this.finish()
//        })
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", {
//            dialogInterface, i ->
//            return@setButton
//        })
//        alertDialog.show()
    }

    private fun setupView() {
        state.adapter = adapterExt.setupAdapter(OrientationHelper.VERTICAL, rvContent, this)
        renderer.render(this, state)
    }
    

    class State : BaseState() {
        var adapter: FastItemAdapter<ViewItem<*>> = FastItemAdapter()
        var itemList: MutableList<ViewItem<*>> = ArrayList()
    }

    class Renderer {

        fun render(activity: TransactionScreen, state: State) {
            renderBody(activity, state)
        }

        fun renderBody(activity: TransactionScreen, state: State) {
            activity.tvBottom.apply {
                text = state.margin
                setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }

            state.itemList.add(AtomicImagesWithLabel.item {
                labelText = "Testing"
                backgroundColor = R.color.colorAccent
                onClickListener = { activity.onBackPressed() }
            })
            state.adapter.set(state.itemList)
        }
    }

    companion object {
    }

}