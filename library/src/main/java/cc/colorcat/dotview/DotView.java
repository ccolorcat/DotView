package cc.colorcat.dotview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Author: cxx
 * Date: 2020-05-13
 * GitHub: https://github.com/ccolorcat
 */
public class DotView extends AppCompatTextView {
    public DotView(Context context) {
        super(context);
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth(), h = getMeasuredHeight();
        // w < h, 确保小红点宽度不小于高度，即可以横向拉伸，但不能纵向拉伸
        // getMinimumWidth() > h 确保重新设置了 size 之后，不会一直处于横向拉伸状态
        if (w < h || getMinimumWidth() > h) {
            setMinimumWidth(h);
            // 设置最小宽度后重新测量，否则文字可能不居中。
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
