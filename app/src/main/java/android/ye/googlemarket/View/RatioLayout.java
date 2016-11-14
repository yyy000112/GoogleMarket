package android.ye.googlemarket.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.ye.googlemarket.R;

/**
 *
 * 自定义控件, 按照比例来决定布局高度
 * Created by ye on 2016/11/11.
 */
public class RatioLayout extends FrameLayout {

    private float ratio;

    public RatioLayout(Context context) {
        super(context);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout );

        ratio = typedArray.getFloat(R.styleable.RatioLayout_ratio,-1);
        typedArray.recycle();

    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获取宽高度和相应的模式
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode==MeasureSpec.EXACTLY && heightMode!=MeasureSpec.EXACTLY &&ratio>0){
            // 图片宽度 = 控件宽度 - 左侧内边距 - 右侧内边距
            int imageWidth = width - getPaddingLeft()-getPaddingRight();
            // 图片高度 = 图片宽度/宽高比例
            int imageHeight = (int) (imageWidth/ratio +0.5f);
            // 控件高度 = 图片高度 + 上侧内边距 + 下侧内边距
            height = imageHeight + getPaddingTop() + getPaddingBottom();
            // 根据最新的高度来重新生成heightMeasureSpec(高度模式是确定模式)
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);

        }
        // 按照最新的高度测量控件
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
