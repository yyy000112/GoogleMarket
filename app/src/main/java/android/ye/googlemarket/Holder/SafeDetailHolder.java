package android.ye.googlemarket.Holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.ye.googlemarket.Http.HttpHelper;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.BitmapHelper;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.domain.AppDetail;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/13.
 */
public class SafeDetailHolder extends BaseHolder<AppDetail>{

    private ImageView[] mSafeIcons;// 安全标识图片
    private ImageView[] mDesIcons;// 安全描述图片
    private TextView[] mSafeDes;// 安全描述文字
    private LinearLayout[] mSafeDesBar;// 安全描述条目(图片+文字)
    private BitmapUtils mBitmapUtils;

    private RelativeLayout rlDesRoot;
    private LinearLayout llDesRoot;
    private ImageView ivArrow;
    private BitmapUtils bitmapUtils;
    private LinearLayout.LayoutParams params;
    private int measuredHeight;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_safeinfo);
        mSafeIcons = new ImageView[4];
        mSafeIcons[0] = (ImageView) view.findViewById(R.id.iv_safe1);
        mSafeIcons[1] = (ImageView) view.findViewById(R.id.iv_safe2);
        mSafeIcons[2] = (ImageView) view.findViewById(R.id.iv_safe3);
        mSafeIcons[3] = (ImageView) view.findViewById(R.id.iv_safe4);

        mDesIcons = new ImageView[4];
        mDesIcons[0] = (ImageView) view.findViewById(R.id.iv_des1);
        mDesIcons[1] = (ImageView) view.findViewById(R.id.iv_des2);
        mDesIcons[2] = (ImageView) view.findViewById(R.id.iv_des3);
        mDesIcons[3] = (ImageView) view.findViewById(R.id.iv_des4);

        mSafeDes = new TextView[4];
        mSafeDes[0] = (TextView) view.findViewById(R.id.tv_des1);
        mSafeDes[1] = (TextView) view.findViewById(R.id.tv_des2);
        mSafeDes[2] = (TextView) view.findViewById(R.id.tv_des3);
        mSafeDes[3] = (TextView) view.findViewById(R.id.tv_des4);

        mSafeDesBar = new LinearLayout[4];
        mSafeDesBar[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
        mSafeDesBar[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
        mSafeDesBar[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
        mSafeDesBar[3] = (LinearLayout) view.findViewById(R.id.ll_des4);

        rlDesRoot = (RelativeLayout) view.findViewById(R.id.rl_des_root);

        llDesRoot = (LinearLayout) view.findViewById(R.id.ll_des_root);
        ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);

        bitmapUtils = BitmapHelper.getBitmapUtils();

        rlDesRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        return view;
    }

    // 标记安全描述开关状态,默认关
    private boolean isOpen =false;

    private void toggle() {
        ValueAnimator animator = null;

        if (isOpen){
            isOpen = false;
            //关闭,属性动画
            animator = ValueAnimator.ofInt(measuredHeight,0);
        }else {
            isOpen = true;
            //打开
            animator = ValueAnimator.ofInt(0, measuredHeight);
        }
        //动画更新监听
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 获取最新的高度值
                Integer mHeight = (Integer) animation.getAnimatedValue();
                //重新修改布局
                params.height = mHeight;
                llDesRoot.setLayoutParams(params);

            }
        });

        //动画监听，改变箭头的方向
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isOpen){
                    ivArrow.setImageResource(R.mipmap.arrow_up);
                }else {
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

    @Override
    public void refreshView(AppDetail data) {
        ArrayList<AppDetail.SafeInfo> safe = data.safe;
        for (int i = 0;i<4;i++){
            if (i<safe.size()){
                // 安全标识图片
                AppDetail.SafeInfo safeInfo = safe.get(i);
                bitmapUtils.display(mDesIcons[i], HttpHelper.URL
                        + "image?name=" + safeInfo.safeDesUrl);
                // 描述文字
                mSafeDes[i].setText(safeInfo.safeDes);
                // 描述图片
                bitmapUtils.display(mSafeIcons[i], HttpHelper.URL
                        + "image?name=" + safeInfo.safeUrl);
            }else {
                // 剩下不应该显示的图片
                mDesIcons[i].setVisibility(View.GONE);
                // 隐藏多余的描述条目
                mSafeDesBar[i].setVisibility(View.GONE);
            }


        }
        //获取安全描述的页面高度
        llDesRoot.measure(0, 0);
        measuredHeight = llDesRoot.getMeasuredHeight();
        // 修改安全描述布局高度为0,达到隐藏效果
        params = (LinearLayout.LayoutParams) llDesRoot.getLayoutParams();
        params.height = 0 ;
        llDesRoot.setLayoutParams(params);

    }
}
