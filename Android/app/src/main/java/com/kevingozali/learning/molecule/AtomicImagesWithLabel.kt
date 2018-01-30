package com.kevingozali.learning.molecule

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import com.bumptech.glide.load.model.GlideUrl
import com.kevingozali.learning.R
import com.kevingozali.learning.fastadapter.ViewItem
import kotlinx.android.synthetic.main.atomic_images_with_label.view.*

/**
 * Created by gozali on 30/01/18.
 */
/**
 * Created by kevingozali on 12/01/18.
 */
open class AtomicImagesWithLabel (context: Context) : RelativeLayout(context) {

    private var state = State()

    init {
        View.inflate(this.context, R.layout.atomic_images_with_label, this)
    }

    fun bind(bind: State.() -> Unit) {
        state.bind()
        render(state, this)
    }

    data class State (
            var labelText: String? = null,
            //var labelTextStyle: Int = R.style.Body_Medium_Blblack,
            var labelLineHeight: Int = 20,
            var imageSource: GlideUrl? = null,
            var imageHeight: Int = 60,
            var imageWidth: Int = 80,
            var labelMaxLines: Int = 2,
            var backgroundColor: Int = R.color.colorPrimary,
            var onClickListener: ((View?) -> Unit)? = null
    )

    fun render(state: State, view: AtomicImagesWithLabel) {

        container.apply {
            state.backgroundColor.let {
                setBackgroundResource(state.backgroundColor)
            }
            state.onClickListener.let {
                setOnClickListener({
                    state.onClickListener?.invoke(view)
                })
            }

        }
        imageView.apply {
            state.imageWidth.let {
                layoutParams.height = state.imageHeight
                layoutParams.width = state.imageWidth
            }

//            state.imageSource.let {
//                setWebDrawable(it)
//            }
        }

        tvLabel.apply {
            state.labelText.let { text = state.labelText }

            //state.labelTextStyle.let { (state.labelTextStyle) }

            state.labelMaxLines.let { maxLines = 2 }

            state.labelLineHeight.let{ setLineSpacing(8f,1f) }
        }
    }

    companion object {
        val classId = AtomicImagesWithLabel::class.java.hashCode()

        fun item(init: AtomicImagesWithLabel.State.() -> Unit): ViewItem<AtomicImagesWithLabel> {
            val state = AtomicImagesWithLabel.State()
            state.init()
            return ViewItem<AtomicImagesWithLabel>(classId) { ctx, _ ->
                val view = AtomicImagesWithLabel(ctx)
                view
            }.withBinder { view, _ ->
                view.render(state, view)
            }
        }
    }
}