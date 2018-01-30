package com.kevingozali.learning.fastadapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

import com.kevingozali.learning.R;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IClickable;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.ISubItem;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.jvm.functions.Function0;

/**
 * Created by kevingozali on 01/30/18.
 */

public class ViewItem<V extends View> extends AbstractItem<ViewItem<V>, ViewWrapper<V>> implements
        IExpandable<ViewItem, ViewItem>, ISubItem<ViewItem, ViewItem>, IClickable<ViewItem<V>> {

    private boolean mExpanded = false;
    private int type;
    private ViewItem mParent;
    private List<ViewItem> mSubItems;
    private ViewGenerator<V> generator;
    private ViewBinder<V> binder, unbinder;
    private Map<Class, Object> typedTags;

    /**
     * This enum used to determine which side to add stroke
     * <p>
     * 1. NO_STROKE : View item will not render stroke in its side
     * <p>
     * 2. SQUARE : View item will render stroke in top, left and right side
     * <p>
     * 3. TOP : View item will render stroke in top, left and right side
     * <p>
     * 4. SIDES : View item will render stroke in left and right side
     * <p>
     * 5. BOTTOM : View item will render stroke in bottom, left and right side
     */
    public enum StrokeType {
        NO_STROKE,
        SQUARE,
        TOP,
        SIDES,
        BOTTOM
    }

    private static final int DEFAULT_STROKE_WIDTH = 2;
    private static final int DEFAULT_STROKE_BACKGROUND_COLOR = R.color.colorPrimary;
    private static final int DEFAULT_STROKE_COLOR = R.color.colorPrimary;

    private StrokeType strokeType = StrokeType.NO_STROKE;

    /**
     * Color to replace current background of View item
     */
    @ColorRes
    private int strokeBackground = DEFAULT_STROKE_BACKGROUND_COLOR;
    @ColorRes
    private int strokeColor = DEFAULT_STROKE_COLOR;
    @Px
    private int strokeWidth = DEFAULT_STROKE_WIDTH;
    private float strokeCornerRadius = 0f;
    private Function0<Boolean> showStroke = null;
    private Drawable mNormalDrawable;

    /**
     * View to rotate when the item is clicked, perfect for expandable animation
     */
    @IdRes private int idViewToRotate;
    private int rotation = 0;

    private OnClickListener<ViewItem<V>> onItemClickListener;
    private OnClickListener<ViewItem<V>> defaultOnItemClickListener = new OnClickListener<ViewItem<V>>() {
        @Override
        public boolean onClick(@Nullable View v, IAdapter<ViewItem<V>> adapter, ViewItem<V> item, int position) {
            View viewToRotate = getViewToRotate(v);
            if (item.getSubItems() != null && viewToRotate != null && rotation != 0) {
                if (item.isExpanded()) {
                    ViewCompat.animate(viewToRotate).rotation(rotation).start();
                } else {
                    ViewCompat.animate(viewToRotate).rotation(0).start();
                }
                return onItemClickListener == null || onItemClickListener.onClick(v, adapter, item, position);
            }
            return onItemClickListener != null && onItemClickListener.onClick(v, adapter, item, position);
        }
    };

    /**
     * Build the view item. there's 2 mandatory arguments: it's type and the view generator
     *
     * @param type      must be globally unique
     * @param generator
     */
    public ViewItem(int type, ViewGenerator<V> generator) {
        this.generator = generator;
        this.type = type;
    }

    /**
     * Build the view item with globally unique type id
     *
     * @param generator
     */
    public ViewItem(ViewGenerator<V> generator) {
        this.generator = generator;
        this.type = generator.hashCode();
    }

    /**
     * Utility fuction to create the list of view items in a declarative way. It also filters out nulls from the output
     *
     * @param items
     * @return
     */
    public static List<AbstractItem> list(AbstractItem... items) {
        ArrayList<AbstractItem> list = new ArrayList<>(items.length);
        for (AbstractItem i : items) {
            if (i == null) continue;
            list.add(i);
        }
        return list;
    }

    /**
     * Utility fuction to create the list of view items in a declarative way. It also filters out nulls from the output
     *
     * @param items
     * @return
     */
    @SafeVarargs
    public static List<AbstractItem> list(List<AbstractItem>... items) {
        ArrayList<AbstractItem> list = new ArrayList<>();
        for (List<AbstractItem> i : items) {
            if (i == null) continue;
            list.addAll(i);
        }
        return list;
    }

    /**
     * set the binder, which set the data to the view
     *
     * @param binder
     * @return
     */
    public ViewItem<V> withBinder(ViewBinder<V> binder) {
        this.binder = binder;
        return this;
    }

    /**
     * @param unbinder
     * @return
     */
    public ViewItem<V> withUnbinder(ViewBinder<V> unbinder) {
        this.unbinder = unbinder;
        return this;
    }

    @Override
    public ViewItem<V> withOnItemClickListener(OnClickListener<ViewItem<V>> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public ViewItem<V> withViewToRotate(@IdRes int id) {
        this.idViewToRotate = id;
        return this;
    }

    public ViewItem<V> withRotation(int rotation) {
        this.rotation = rotation;
        return this;
    }

    public ViewItem<V> withStroke(StrokeType strokeType, Function0<Boolean> showStroke) {
        return withStroke(strokeType, DEFAULT_STROKE_BACKGROUND_COLOR, DEFAULT_STROKE_COLOR,
                DEFAULT_STROKE_WIDTH, 0f, showStroke);
    }

    public ViewItem<V> withStroke(StrokeType strokeType, @ColorRes int replaceBackgroundColor, Function0<Boolean> showStroke) {
        return withStroke(strokeType, replaceBackgroundColor, DEFAULT_STROKE_COLOR,
                DEFAULT_STROKE_WIDTH, 0f, showStroke);
    }

    public ViewItem<V> withStroke(StrokeType strokeType, @ColorRes int replaceBackgroundColor,
                                  @ColorRes int strokeColor, Function0<Boolean> showStroke) {
        return withStroke(strokeType, replaceBackgroundColor, strokeColor,
                DEFAULT_STROKE_WIDTH, 0f, showStroke);
    }

    public ViewItem<V> withStroke(StrokeType strokeType, @ColorRes int replaceBackgroundColor,
                                  @ColorRes int strokeColor, @Px int strokeWidth, Function0<Boolean> showStroke) {
        return withStroke(strokeType, replaceBackgroundColor, strokeColor, strokeWidth, 0f, showStroke);
    }

    public ViewItem<V> withStroke(StrokeType strokeType, @ColorRes int replaceBackgroundColor,
                                  @ColorRes int strokeColor, @Px int strokeWidth, float strokeCornerRadius,
                                  Function0<Boolean> showStroke) {
        this.strokeType = strokeType;
        this.showStroke = showStroke;
        this.strokeBackground = replaceBackgroundColor;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.strokeCornerRadius = strokeCornerRadius;
        return this;
    }

    @Override
    public OnClickListener<ViewItem<V>> getOnItemClickListener() {
        return defaultOnItemClickListener;
    }

    /**
     * Do not use.
     * @return
     */
    @Override
    public int getLayoutRes() {
        return 0; //this is ignored, we're overriding generateView instead
    }

    /**
     * The type of view item. it is globally unique
     * @return
     */
    @Override
    public int getType() {
        return type;
    }

    /**
     * The logic to bind our data to the view
     */
    @Override
    public void bindView(ViewWrapper<V> holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        if (binder != null) {
            binder.bindView(holder.view, this);
            mNormalDrawable = holder.view.getBackground();
            bindStroke(holder.view);
        }
        View viewToRotate = getViewToRotate(holder.view);
        if (viewToRotate != null && rotation != 0 && isExpanded()) {
            viewToRotate.setRotation(rotation);
        } else if (viewToRotate != null) {
            viewToRotate.setRotation(0);
        }
    }

    /**
     * set the identifier of this item
     *
     * @param identifier
     * @return
     */
    public ViewItem<V> withIdentifier(String identifier) {
        this.mIdentifier = identifier.hashCode();
        return this;
    }

    /**
     * This optional state unbinds the view to the default state
     *
     * @param holder
     */
    @Override
    public void unbindView(ViewWrapper<V> holder) {
        super.unbindView(holder);
        unbindStroke(holder.view);
        if (unbinder != null) {
            unbinder.bindView(holder.view, this);
        }
        View viewToRotate = getViewToRotate(holder.view);
        if (viewToRotate != null) {
            viewToRotate.clearAnimation();
        }
    }

    private void bindStroke(View view) {
        if (strokeType != StrokeType.NO_STROKE) {
            if (showStroke != null && showStroke.invoke()) {
                int paddingLeft = view.getPaddingLeft();
                int paddingTop = view.getPaddingTop();
                int paddingRight = view.getPaddingRight();
                int paddingBottom = view.getPaddingBottom();
                view.setBackground(getDrawableStroke(view));
                view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            } else {
                view.setBackground(mNormalDrawable);
            }
        }
    }

    private void unbindStroke(View view) {
        if (strokeType != StrokeType.NO_STROKE) {
            view.setBackground(mNormalDrawable);
            mNormalDrawable = null;
        }
    }

    private LayerDrawable getDrawableStroke(View view) {
        GradientDrawable layer1 = buildStrokeDrawable(view.getContext(), strokeColor);
        GradientDrawable layer2 = buildStrokeDrawable(view.getContext(), strokeBackground);

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] {layer1, layer2});
        float[] corners = new float[8];
        Arrays.fill(corners, 0f);
        switch (strokeType) {
            case TOP:
                Arrays.fill(corners, 0, 4, strokeCornerRadius);
                layer1.setCornerRadii(corners);
                layer2.setCornerRadii(corners);
                layerDrawable.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, 0);
                break;
            case SIDES:
                layer1.setCornerRadius(0f);
                layer2.setCornerRadius(0f);
                layerDrawable.setLayerInset(1, strokeWidth, 0, strokeWidth, 0);
                break;
            case BOTTOM:
                Arrays.fill(corners, 4, 8, strokeCornerRadius);
                layer1.setCornerRadii(corners);
                layer2.setCornerRadii(corners);
                layerDrawable.setLayerInset(1, strokeWidth, 0, strokeWidth, strokeWidth);
                break;
            case SQUARE:
                layer1.setCornerRadius(strokeCornerRadius);
                layer2.setCornerRadius(strokeCornerRadius);
                layerDrawable.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth);
                break;
        }
        return layerDrawable;
    }

    private GradientDrawable buildStrokeDrawable(Context context, @ColorRes int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(ContextCompat.getColor(context, color));
        drawable.mutate();
        return drawable;
    }

    /**
     * Generates the view for which this view item
     * @param ctx
     * @param parent
     * @return
     */
    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        return generator.generateView(ctx, parent);
    }

    /**
     * @param parent
     * @return
     * @deprecated Generate a view holder. do not use this
     */
    @Deprecated
    @Override
    public ViewWrapper<V> getViewHolder(ViewGroup parent) {
        return getViewHolder(generateView(parent.getContext(), parent));
    }

    /**
     * @param v
     * @return
     * @deprecated Generate a view holder. do not use this
     */
    @Deprecated
    @Override
    public ViewWrapper<V> getViewHolder(View v) {
        return new ViewWrapper<>(v);
    }

    private View getViewToRotate(View parent) {
        return parent.findViewById(idViewToRotate);
    }

    /**
     * Is the View expanded or not. All view items are assumed to be expandable
     * @return
     */
    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    /**
     * Set the view item as expanded or not. The view will change after the recyclerview is notified or the view is recycled
     * @param collapsed
     * @return
     */
    @Override
    public ViewItem<V> withIsExpanded(boolean collapsed) {
        mExpanded = collapsed;
        return this;
    }

    /**
     * Set the view's subitems. The view will change after the recyclerview is notified or the view is recycled
     * @param subItems
     * @return
     */
    @Override
    public ViewItem<V> withSubItems(List<ViewItem> subItems) {
        this.mSubItems = subItems;
        for (ViewItem subItem : subItems) {
            subItem.withParent(this);
        }
        return this;
    }

    /**
     * It's exactly like withSubItems, but this one support variadic parameters for better visual
     * @param items
     * @return
     */
    public ViewItem<V> withSubItemsOf(ViewItem... items) {
        return withSubItems(Arrays.asList(items));
    }

    /**
     * get the view's subitems
     * @return
     */
    @Override
    public List<ViewItem> getSubItems() {
        return this.mSubItems;
    }

    /**
     * this is always true.
     * @return
     */
    @Override
    public boolean isAutoExpanding() {
        return true;
    }

    /**
     * get the parent of this view if it is a subitem. a top level item would return null.
     * @return
     */
    @Override
    public ViewItem<V> getParent() {
        return mParent;
    }

    /**
     * @deprecated
     * Do not use this. use the parent's withSubItems instead.
     * @param parent
     * @return
     */
    @Deprecated
    @Override
    public ViewItem withParent(ViewItem parent) {
        mParent = parent;
        return this;
    }

    /**
     * Add tag of arbitrary type. This object can contain multiple tags as long as each key (class)
     * is different. Tags set with this method will not collide with one that's set with
     * {@link #withTag(Object)}.
     *
     * @return this object, for chaining
     */
    public <T> ViewItem<V> withTag(Class<T> key, T value) {
        if (typedTags == null) { // lazy creation
            synchronized (this) { // thread-safe
                if (typedTags == null) { // there's a small chance it's been set
                    typedTags = new HashMap<>(2);
                }
            }
        }
        typedTags.put(key, value);
        return this;
    }

    /**
     * Get the tag set by {@link #withTag(Class, Object)} or null if not exists. Note that the key
     * (class) must exactly the same, a class is considered different to its superclass.
     */
    public <T> T getTag(Class<T> key) {
        if (typedTags == null) {
            return null;
        }
        //noinspection unchecked
        return (T) typedTags.get(key);
    }
}
