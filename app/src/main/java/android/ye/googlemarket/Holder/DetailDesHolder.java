package android.ye.googlemarket.Holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.BitmapHelper;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.domain.AppDetail;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by ye on 2016/11/14.
 */
public class DetailDesHolder extends BaseHolder<AppDetail> {

    private TextView tvDesDetail;
    private TextView tvAuthor;
    private ImageView ivArrow;
    private RelativeLayout rlToggle;
    private LinearLayout.LayoutParams params;



    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_des);
        tvDesDetail = (TextView) view.findViewById(R.id.tv_infoDes);
        tvAuthor = (TextView) view.findViewById(R.id.tv_detail_author);
        ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
        rlToggle = (RelativeLayout) view.findViewById(R.id.rl_detail_toggle);
        rlToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    private boolean isOpen = false;
    private void toggle() {
        int shortHeight =  getShortHeight();
        int longHeight = getLongHeight();
        ValueAnimator animator = null;
        if (isOpen){
            isOpen = false;
            if (longHeight>shortHeight){
                //关闭,属性动画
                animator = ValueAnimator.ofInt(longHeight,shortHeight);
            }

        }else {
            isOpen = true;
            // 只有描述信息大于7行,才启动动画
            if (longHeight>shortHeight){
                animator = ValueAnimator.ofInt(shortHeight,longHeight);
            }

        }

        if (animator!=null) {


            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // 获取最新的高度值
                    Integer mHeight = (Integer) animation.getAnimatedValue();
                    //重新修改布局

                    params.height = mHeight;
                    tvDesDetail.setLayoutParams(params);
                }
            });

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //设置ScrollView在打开文字开关时自动滚到底部
                   final ScrollView scrollView =  getScrollView();
                    // 为了运行更加安全和稳定, 可以讲滑动到底部方法放在消息队列中执行
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            // 滚动到底部
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    if (isOpen) {
                        ivArrow.setImageResource(R.mipmap.arrow_up);
                    } else {
                        ivArrow.setImageResource(R.mipmap.arrow_down);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.setDuration(200);
            animator.start();
        }


    }

    /**
     * 获取ScrollView, 一层一层往上找,
     * 知道找到ScrollView后才返回;
     * 注意:一定要保证父控件或祖宗控件有ScrollView,否则死循环
     * @return
     */
    private ScrollView getScrollView() {
        ViewParent parent = tvDesDetail.getParent();
        while (!(parent instanceof ScrollView)){
            parent = parent.getParent();
        }
        return (ScrollView) parent;
    }

    //测量无行数显示限制的高度
    private int getLongHeight() {
        //获得宽度
        int width =tvDesDetail.getMeasuredWidth();
        //设置一个虚拟TextView来测量显示7行文字的高度
        TextView view = new TextView(UIUtils.getContext());
        view.setText(getData().des);
        //保持字体一致
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        int measureWidthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        //设置的高度值大于等于屏幕高度就行
        int measureHeightSpec = View.MeasureSpec.makeMeasureSpec(3000, View.MeasureSpec.AT_MOST);
        //测量高度
        view.measure(measureWidthSpec,measureHeightSpec);
        //返回测量后的高度
        return view.getMeasuredHeight();

    }

    //测量有行数显示限制的高度
    private int getShortHeight() {
        //获得宽度
        int width =tvDesDetail.getMeasuredWidth();
        //设置一个虚拟TextView来测量显示7行文字的高度
        TextView view = new TextView(UIUtils.getContext());
        view.setText(getData().des);
        //保持字体一致
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        //设置最大行数
        view.setMaxLines(6);

        int measureWidthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        //设置的高度值大于等于屏幕高度就行
        int measureHeightSpec = View.MeasureSpec.makeMeasureSpec(3000, View.MeasureSpec.AT_MOST);
        //测量高度
        view.measure(measureWidthSpec,measureHeightSpec);
        //返回测量后的高度
        return view.getMeasuredHeight();
    }

    @Override
    public void refreshView(AppDetail data) {
        tvDesDetail.setText(data.des);
        tvAuthor.setText(data.author);
        // 放在消息队列中运行, 以免出现不足7行时显示7行高度问题
        tvDesDetail.post(new Runnable() {
            @Override
            public void run() {
                //默认展示6行高度
                int shortHeight = getShortHeight();
                params = (LinearLayout.LayoutParams) tvDesDetail.getLayoutParams();
                params.height = shortHeight;
                tvDesDetail.setLayoutParams(params);
            }
        });


    }
}
