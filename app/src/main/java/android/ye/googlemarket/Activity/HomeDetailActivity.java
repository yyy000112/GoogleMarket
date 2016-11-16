package android.ye.googlemarket.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.ye.googlemarket.Holder.AppDetailHolder;
import android.ye.googlemarket.Holder.DetailDesHolder;
import android.ye.googlemarket.Holder.DetailDownHolder;
import android.ye.googlemarket.Holder.HorizontalHolder;
import android.ye.googlemarket.Holder.SafeDetailHolder;
import android.ye.googlemarket.Http.protocol.HomeDetailProtocol;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.LogUtils;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.View.LoadingPager;
import android.ye.googlemarket.domain.AppDetail;

/**
 *
 * 应用详情页
 * Created by ye on 2016/11/13.
 */
public class HomeDetailActivity extends BaseActivity{

    private LoadingPager mLoadingPager;
    private String packageName;
    private AppDetail data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingPager = new LoadingPager(UIUtils.getContext()) {
            @Override
            public View onCreateSuccessView() {
                View view = UIUtils.inflate(R.layout.page_home_detail);
                //加载app详情
                FrameLayout flAppDetail = (FrameLayout) view.findViewById(R.id.fl_detail_appinfo);
                AppDetailHolder appDetailHolder = new AppDetailHolder();
                flAppDetail.addView(appDetailHolder.getRootView());
                appDetailHolder.setData(data);
                //加载安全详情页
                FrameLayout flSafeDetail = (FrameLayout) view.findViewById(R.id.fl_detail_safe);
                SafeDetailHolder safeDetailHolder = new SafeDetailHolder();
                flSafeDetail.addView(safeDetailHolder.getRootView());
                safeDetailHolder.setData(data);
                //加载截图滑动
                HorizontalScrollView hlScroll = (HorizontalScrollView) view.findViewById(R.id.hl_scroll);
                HorizontalHolder horizontalHolder = new HorizontalHolder();
                hlScroll.addView(horizontalHolder.getRootView());
                horizontalHolder.setData(data);
                //加载详细描述
                FrameLayout flDetailDes = (FrameLayout) view.findViewById(R.id.fl_detail_des);
                DetailDesHolder detailDesHolder = new DetailDesHolder();
                flDetailDes.addView(detailDesHolder.getRootView());
                detailDesHolder.setData(data);

                //加载下载布局
                FrameLayout flDownload = (FrameLayout) view.findViewById(R.id.fl_detail_download);
                DetailDownHolder detailDownHolder = new DetailDownHolder();
                flDownload.addView(detailDownHolder.getRootView());
                detailDownHolder.setData(data);
                return view;
            }

            @Override
            public ResultState onLoad() {
                HomeDetailProtocol protocol = new HomeDetailProtocol(packageName);
                data = protocol.getData(0);


                if (data != null) {
                    return ResultState.STATE_SUCCESS;
                } else {
                    return ResultState.STATE_ERROR;
                }
            }
        };
        // 直接将一个view对象设置给activity
        setContentView(mLoadingPager);

        packageName = getIntent().getStringExtra("packageName");

        mLoadingPager.loadData();
    }


}
