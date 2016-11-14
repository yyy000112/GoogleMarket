package android.ye.googlemarket.Holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.ye.googlemarket.Http.HttpHelper;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.BitmapHelper;
import android.ye.googlemarket.Utils.UIUtils;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/13.
 */
public class HomeHeaderHolder extends BaseHolder<ArrayList<String>> {

    private ViewPager mViewPager;
    ArrayList<String> data;
    private LinearLayout llContainer;
    private ImageView point;

    private int mPreviousPos;

    @Override
    public View initView() {
        // 创建根布局, 相对布局
        RelativeLayout rlView = new RelativeLayout(UIUtils.getContext());
        // 初始化布局参数, 根布局上层控件是listview, 所以要使用listview定义的LayoutParams
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,UIUtils.dip2px(150));
        rlView.setLayoutParams(params);
        //设置ViewPager
        mViewPager = new ViewPager(UIUtils.getContext());
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //将ViewPager添加给relativelayout
        rlView.addView(mViewPager, params1);

        //添加指示器
        llContainer = new LinearLayout(UIUtils.getContext());
        llContainer.setOrientation(LinearLayout.HORIZONTAL);
        //通过父类布局设置参数
        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置内边距
        int padding = UIUtils.dip2px(10);
        llContainer.setPadding(padding,padding,padding,padding);

        llParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        llParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        rlView.addView(llContainer,llParams);

        return rlView;
    }

    @Override
    public void refreshView(final ArrayList<String> data) {

        this.data = data;
        mViewPager.setAdapter(new HomeHeaderAdapter());
        mViewPager.setCurrentItem(data.size()*10000);
        //初始化指示器
        for (int i = 0 ; i< data.size();i++){
            point = new ImageView(UIUtils.getContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (i == 0){
                //默认第一页选中
                point.setImageResource(R.mipmap.indicator_selected);
            }else {
                point.setImageResource(R.mipmap.indicator_normal);
                //设置指示器边距
                params.leftMargin = UIUtils.dip2px(4);
            }
            point.setLayoutParams(params);
            llContainer.addView(point);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % data.size();
                point = (ImageView) llContainer.getChildAt(position);
                point.setImageResource(R.mipmap.indicator_selected);


                ImageView prePoint = (ImageView) llContainer.getChildAt(mPreviousPos);
                prePoint.setImageResource(R.mipmap.indicator_normal);
                mPreviousPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //实现自动轮播
        HomeHeaderTask task = new HomeHeaderTask();
        task.start();
    }

     class HomeHeaderAdapter extends PagerAdapter {

         private final BitmapUtils bitmapUtils;

         public HomeHeaderAdapter(){
             bitmapUtils = BitmapHelper.getBitmapUtils();
         }
         @Override
         public int getCount() {
             //return data.size();
             //实现无限轮播
             return Integer.MAX_VALUE;
         }

         @Override
         public boolean isViewFromObject(View view, Object object) {
             return view == object;
         }

         @Override
         public Object instantiateItem(ViewGroup container, int position) {
            position = position%data.size();
             String url = data.get(position);
             ImageView view = new ImageView(UIUtils.getContext());
             view.setScaleType(ImageView.ScaleType.FIT_XY);
             bitmapUtils.display(view, HttpHelper.URL + "image?name=" + url);

             container.addView(view);
             return view;
         }

         @Override
         public void destroyItem(ViewGroup container, int position, Object object) {
             container.removeView((View) object);
         }
     }

     class HomeHeaderTask implements Runnable {

         @Override
         public void run() {
            int currentItem = mViewPager.getCurrentItem();
             currentItem++;
             mViewPager.setCurrentItem(currentItem);
             // 继续发延时3秒消息, 实现内循环
             UIUtils.getHandler().postDelayed(this, 3000);
         }

         public void start() {
             // 移除之前发送的所有消息, 避免消息重复
            UIUtils.getHandler().removeCallbacksAndMessages(null);
            UIUtils.getHandler().postDelayed(this,3000);

         }
     }
}
