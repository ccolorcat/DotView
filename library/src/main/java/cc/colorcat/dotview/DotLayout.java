package cc.colorcat.dotview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

/**
 * Author: cxx
 * Date: 2020-05-12
 * GitHub: https://github.com/ccolorcat
 * <p>
 * for (int i = 0; i < tabs.getTabCount(); i++) {
 * TabLayout.Tab tab = tabs.getTabAt(i);
 * if (tab == null) continue;
 * View tabView = Utils.nullElse(tab.getCustomView(), tab.view);
 * if (tabView == null) continue;
 * DotLayout dotLayout = DotLayout.inject(tabView, false);
 * dotLayout.setDotViewGravity(Gravity.CENTER_VERTICAL | Gravity.END);
 * }
 */
public class DotLayout extends FrameLayout {
    private static final int DEFAULT_MAX_COUNT = 99;

    private int mMaxCount = DEFAULT_MAX_COUNT;
    private int mCount;
    private boolean mAutoHide = true;
    private TextView mDotView;
    private View mDotViewContainer;

    public DotLayout(@NonNull Context context) {
        this(context, null);
    }

    public DotLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DotLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DotLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private DotLayout(@NonNull Context context, @LayoutRes int dotViewLayout, boolean autoHide) {
        super(context);
        mAutoHide = autoHide;
        inflate(context, dotViewLayout);
        resetDotMsg();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DotLayout);
        mAutoHide = ta.getBoolean(R.styleable.DotLayout_autoHide, true);
        mMaxCount = ta.getInteger(R.styleable.DotLayout_maxCount, DEFAULT_MAX_COUNT);
        int layout = ta.getResourceId(R.styleable.DotLayout_dotViewLayout, R.layout.widget_dot_view_layout);
        float size = ta.getDimension(R.styleable.DotLayout_dotViewSize, -1F);
        int gravity = ta.getInt(R.styleable.DotLayout_dotViewLayoutGravity, -1);
        ta.recycle();
        inflate(context, layout);
        if (size > 0F) setDotViewTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        if (gravity != -1) setDotViewGravity(gravity);
        resetDotMsg();
    }

    private void inflate(Context context, @LayoutRes int dotLayout) {
        mDotViewContainer = LayoutInflater.from(context).inflate(dotLayout, this, false);
        mDotView = mDotViewContainer.findViewById(R.id.widget_dot_view);
        addView(mDotViewContainer);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child != mDotViewContainer) {
            clearStateListAnimator(child);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int index = indexOfChild(mDotViewContainer);
        if (index != getChildCount() - 1) {
            mDotViewContainer.bringToFront();
        }
    }

    public void setDotViewMargin(int unit, int margin) {
        setDotViewMargin(unit, margin, margin, margin, margin);
    }

    public void setDotViewMargin(int unit, int start, int top, int end, int bottom) {
        LayoutParams params = getDotViewLayoutParams();
        params.setMarginStart(toPx(unit, start));
        params.topMargin = toPx(unit, top);
        params.setMarginEnd(toPx(unit, end));
        params.bottomMargin = toPx(unit, bottom);
        setLayoutParams(params);
    }

    public void setDotViewMarginStart(int unit, int start) {
        LayoutParams params = getDotViewLayoutParams();
        params.setMarginStart(toPx(unit, start));
        setLayoutParams(params);
    }

    public void setDotViewMarginTop(int unit, int top) {
        LayoutParams params = getDotViewLayoutParams();
        params.topMargin = toPx(unit, top);
        setLayoutParams(params);
    }

    public void setDotViewMarginEnd(int unit, int end) {
        LayoutParams params = getDotViewLayoutParams();
        params.setMarginEnd(toPx(unit, end));
        setLayoutParams(params);
    }

    public void setDotViewMarginBottom(int unit, int bottom) {
        LayoutParams params = getDotViewLayoutParams();
        params.bottomMargin = toPx(unit, bottom);
        setLayoutParams(params);
    }

    public void setDotViewPadding(int unit, int padding) {
        setDotViewPadding(unit, padding, padding, padding, padding);
    }

    public void setDotViewPadding(int unit, int start, int top, int end, int bottom) {
        mDotView.setPaddingRelative(
                toPx(unit, start),
                toPx(unit, top),
                toPx(unit, end),
                toPx(unit, bottom)
        );
    }

    public void setDotViewPaddingStart(int unit, int start) {
        mDotView.setPaddingRelative(
                toPx(unit, start),
                mDotView.getPaddingTop(),
                mDotView.getPaddingEnd(),
                mDotView.getPaddingBottom()
        );
    }

    public void setDotViewPaddingTop(int unit, int top) {
        mDotView.setPaddingRelative(
                mDotView.getPaddingStart(),
                toPx(unit, top),
                mDotView.getPaddingEnd(),
                mDotView.getPaddingBottom()
        );
    }

    public void setDotViewPaddingEnd(int unit, int end) {
        mDotView.setPaddingRelative(
                mDotView.getPaddingStart(),
                mDotView.getPaddingTop(),
                toPx(unit, end),
                mDotView.getPaddingBottom()
        );
    }

    public void setDotViewPaddingBottom(int unit, int bottom) {
        mDotView.setPaddingRelative(
                mDotView.getPaddingStart(),
                mDotView.getPaddingTop(),
                mDotView.getPaddingEnd(),
                toPx(unit, bottom)
        );
    }

    public void setDotViewGravity(int gravity) {
        LayoutParams params = getDotViewLayoutParams();
        params.gravity = gravity;
        setDotViewLayoutParams(params);
    }

    public DotLayout.LayoutParams getDotViewLayoutParams() {
        return (LayoutParams) mDotViewContainer.getLayoutParams();
    }

    public void setDotViewLayoutParams(DotLayout.LayoutParams params) {
        mDotViewContainer.setLayoutParams(params);
    }

    public void setDotViewBackground(Drawable drawable) {
        ViewCompat.setBackground(mDotView, drawable);
    }

    public void setDotViewBackground(@DrawableRes int resId) {
        mDotView.setBackgroundResource(resId);
    }

    public void setDotViewTextSize(int unit, float size) {
        mDotView.setTextSize(unit, size);
    }

    public void setDotViewSize(int unit, int size) {
        ViewGroup.LayoutParams params = mDotView.getLayoutParams();
        params.height = toPx(unit, size);
        mDotView.setLayoutParams(params);
    }

    public void setAutoHideEnabled(boolean enabled) {
        if (mAutoHide != enabled) {
            mAutoHide = enabled;
            resetDotMsg();
        }
    }

    public void showDotView() {
        setDotViewVisibility(true);
    }

    public void hideDotView() {
        setDotViewVisibility(false);
    }

    public void setDotViewVisibility(boolean visible) {
        mDotViewContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void clearDotView() {
        mCount = 0;
        mDotView.setText("");
    }

    public void setMaxCount(int maxCount) {
        // 不判断 mMaxCount != maxCount 是确保 resetDotMsg 可以执行
        if (maxCount > 0) {
            mMaxCount = maxCount;
            resetDotMsg();
        }
    }

    public void setCount(int count) {
        // 不判断 mCount != count 是确保 resetDotMsg 可以执行
        if (count >= 0) {
            mCount = count;
            resetDotMsg();
        }
    }

    public int getCount() {
        return mCount;
    }

    public void increaseCount() {
        if (mCount < Integer.MAX_VALUE) {
            ++mCount;
            resetDotMsg();
        }
    }

    public void decreaseCount() {
        if (mCount > 0) {
            --mCount;
            resetDotMsg();
        }
    }

    private void resetDotMsg() {
        if (mAutoHide && mCount <= 0) {
            mDotViewContainer.setVisibility(View.GONE);
        } else {
            mDotViewContainer.setVisibility(View.VISIBLE);
            String msg = mCount > mMaxCount ? mMaxCount + "+" : (mCount <= 0 ? "" : Integer.toString(mCount));
            mDotView.setText(msg);
        }
    }

    private DisplayMetrics mMetrics;

    private int toPx(int unit, int value) {
        if (mMetrics == null) mMetrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(unit, value, mMetrics);
    }

    /**
     * 在 child 上显示红点
     *
     * @param child    要求 child 必须有一个父布局。
     * @param autoHide 如果为 {@code true}，则当 count 降为 0 则红点自动消失
     * @see DotView
     */
    public static DotLayout inject(@NonNull View child, boolean autoHide) {
        return inject(child, -1, autoHide);
    }

    /**
     * 在 child 上显示红点
     *
     * @param child           要求 child 必须有一个父布局。
     * @param dotViewLayoutId 自定义的红点效果，要求布局内必须有 {@link TextView} 或其子类，
     *                        且 id 为 widget_dot_view, 可参考 R.layout.widget_dot_view_layout
     * @param autoHide        如果为 {@code true}，则当 count 降为 0 则红点自动消失
     * @see DotView
     */
    public static DotLayout inject(@NonNull View child, @LayoutRes int dotViewLayoutId, boolean autoHide) {
        int layoutId = dotViewLayoutId != -1 ? dotViewLayoutId : R.layout.widget_dot_view_layout;
        ViewGroup parent = getParent(child);
        DotLayout dotLayout = new DotLayout(child.getContext(), layoutId, autoHide);
        // 以下针对默认宽高为 wrap_content 的 View，如 Button 默认存在最小宽、高。
        dotLayout.setMinimumHeight(ViewCompat.getMinimumHeight(child));
        dotLayout.setMinimumWidth(ViewCompat.getMinimumWidth(child));
        dotLayout.setId(ViewCompat.generateViewId());
        if (canOverlay(parent)) {
            clearStateListAnimator(child);
            parent.addView(dotLayout, child.getLayoutParams());
            return dotLayout;
        }

        final int childIndex = parent.indexOfChild(child);
        parent.removeViewAt(childIndex);
        parent.addView(dotLayout, childIndex, child.getLayoutParams());
        LayoutParams newChildLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        dotLayout.addView(child, 0, newChildLp);
        return dotLayout;
    }

    /**
     * {@link ConstraintLayout} 和 {@link RelativeLayout}，其内部 View 的位置相互存在依赖，
     * 故不能随意移除，{@link FrameLayout} 其实可以不用加在这里的。
     */
    private static boolean canOverlay(ViewGroup group) {
        return group instanceof ConstraintLayout
                || group instanceof RelativeLayout
                || group instanceof FrameLayout;
    }

    private static ViewGroup getParent(View child) {
        ViewParent parent = child.getParent();
        if (!(parent instanceof ViewGroup)) {
            throw new IllegalArgumentException("the child " + child + " must be have a ViewParent.");
        }
        return (ViewGroup) parent;
    }

    private static void clearStateListAnimator(View child) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            child.setStateListAnimator(null);
        }
    }
}
