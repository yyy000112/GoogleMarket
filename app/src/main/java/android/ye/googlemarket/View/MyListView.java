package android.ye.googlemarket.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by ye on 2016/11/11.
 */
public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
        initView();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        // 设置默认状态选择器为全透明
        this.setSelector(new ColorDrawable());
        // 去掉分隔线
        this.setDivider(null);
        // 有时候滑动listview背景会变成黑色,此方法将背景变为全透明
        this.setCacheColorHint(Color.TRANSPARENT);
    }

}
