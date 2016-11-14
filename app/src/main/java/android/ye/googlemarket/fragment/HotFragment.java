package android.ye.googlemarket.fragment;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.ye.googlemarket.Http.protocol.HomeProtocol;
import android.ye.googlemarket.Http.protocol.HotProtocol;
import android.ye.googlemarket.Utils.DrawableUtils;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.View.FlowLayout;
import android.ye.googlemarket.View.LoadingPager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ye on 2016/11/9.
 */
public class HotFragment extends BaseFragment {

    private ArrayList<String> data;

    @Override
    public View onCreateSuccessView() {
        ScrollView scrollView = new ScrollView(UIUtils.getContext());
        FlowLayout flowLayout = new FlowLayout(UIUtils.getContext());
        int padding = UIUtils.dip2px(10);
        flowLayout.setPadding(padding,padding,padding,padding);
        //水平间距
        flowLayout.setHorizontalSpacing(UIUtils.dip2px(6));
        //垂直间距
        flowLayout.setVerticalSpacing(UIUtils.dip2px(8));
        for (int i= 0; i<data.size();i++){
            final String keyword= data.get(i);
            TextView view = new TextView(UIUtils.getContext());
            view.setText(keyword);
            view.setTextColor(Color.WHITE);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            view.setGravity(Gravity.CENTER);
            //生成随机颜色
            Random random = new Random();
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);

            int color = 0xffcecece;// 按下后偏白的背景色
            StateListDrawable selector = DrawableUtils.getSelector(Color.rgb(r,g,b),color,UIUtils.dip2px(6));
            view.setBackgroundDrawable(selector);
            flowLayout.addView(view);
            // 只有设置点击事件, 状态选择器才起作用
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.ToastShow(keyword);
                }
            });
        }
        scrollView.addView(flowLayout);
        return scrollView;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        HotProtocol protocol = new HotProtocol();
        data = protocol.getData(0);
        return check(data);
    }
}
