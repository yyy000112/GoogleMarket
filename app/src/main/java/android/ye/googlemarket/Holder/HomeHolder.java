package android.ye.googlemarket.Holder;

import android.app.ActivityManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.ye.googlemarket.Http.HttpHelper;
import android.ye.googlemarket.Manager.DownloadManager;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.BitmapHelper;
import android.ye.googlemarket.Utils.LogUtils;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.View.ProgressArc;
import android.ye.googlemarket.domain.AppDetail;
import android.ye.googlemarket.domain.AppInfo;
import android.ye.googlemarket.domain.DownLoadInfo;
import android.ye.googlemarket.domain.HomeList0;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by ye on 2016/11/10.
 */
//public class HomeHolder extends BaseHolder<HomeList0.List> implements DownloadManager.DownloadObserver,View.OnClickListener 数据重新设置
public class HomeHolder extends BaseHolder<AppDetail> implements DownloadManager.DownloadObserver,View.OnClickListener {

    private View view;
    private ImageView ivIcon;
    private TextView tvName;
    private RatingBar rbStar;
    private TextView tvSize;
    private TextView tvDes;
    private BitmapUtils mBitmapUtils;
    private ProgressArc pbProgress;
    private DownloadManager mDM;
    private int mCurrentState;
    private float mProgress;
    private TextView tvDownload;


    @Override
    public View initView() {
        view = UIUtils.inflate(R.layout.list_item_home);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        rbStar = (RatingBar) view.findViewById(R.id.rb_star);
        tvSize = (TextView) view.findViewById(R.id.tv_size);
        tvDes = (TextView) view.findViewById(R.id.tv_des);

        tvDownload = (TextView) view.findViewById(R.id.tv_download);
        mBitmapUtils = BitmapHelper.getBitmapUtils();

        FrameLayout flProgress = (FrameLayout) view.findViewById(R.id.fl_progress);
        flProgress.setOnClickListener(this);
        pbProgress = new ProgressArc(UIUtils.getContext());
        // 设置圆形进度条直径
        pbProgress.setArcDiameter(UIUtils.dip2px(26));
        // 设置进度条颜色
        pbProgress.setProgressColor(UIUtils.getColor(R.color.progress));
        // 设置进度条宽高布局参数
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                UIUtils.dip2px(27), UIUtils.dip2px(27));
        flProgress.addView(pbProgress, params);

        mDM = DownloadManager.getInstance();
        mDM.registerObserver(this);// 注册观察者, 监听状态和进度变化
        return view;
    }

    @Override
    public void refreshView(AppDetail data) {
        tvName.setText(data.name);
        rbStar.setRating(data.stars);
        tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
        tvDes.setText(data.des);
        mBitmapUtils.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);


        // 判断当前应用是否下载过
        DownLoadInfo downloadInfo = mDM.getDownloadInfo(data);
        if (downloadInfo != null) {
            // 之前下载过
            mCurrentState = downloadInfo.currentState;
            mProgress = downloadInfo.getProgress();
        } else {
            // 没有下载过
            mCurrentState = DownloadManager.STATE_UNDO;
            mProgress = 0;
        }

        refreshUI(mCurrentState, mProgress, data.id);
    }

    private void refreshUI(int state, float progress, String id) {
        // 由于listview重用机制, 要确保刷新之前, 确实是同一个应用
        if (!getData().id.equals(id)) {
            return;
        }

        mCurrentState = state;
        mProgress = progress;
        switch (state) {
            case DownloadManager.STATE_UNDO:
                // 自定义进度条背景
                pbProgress.setBackgroundResource(R.mipmap.ic_download);
                // 没有进度
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("下载");
                break;
            case DownloadManager.STATE_WAIT:
                pbProgress.setBackgroundResource(R.mipmap.ic_download);
                // 等待模式
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
                tvDownload.setText("等待");
                break;
            case DownloadManager.STATE_LOADING:
                pbProgress.setBackgroundResource(R.mipmap.ic_pause);
                // 下载中模式
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                pbProgress.setProgress(progress, true);
                tvDownload.setText((int) (progress * 100) + "%");
                break;
            case DownloadManager.STATE_PAUSE:
                pbProgress.setBackgroundResource(R.mipmap.ic_resume);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                break;
            case DownloadManager.STATE_FAILURE:
                pbProgress.setBackgroundResource(R.mipmap.ic_redownload);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("下载失败");
                break;
            case DownloadManager.STATE_SUCCESS:
                pbProgress.setBackgroundResource(R.mipmap.ic_install);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("安装");
                break;

            default:
                break;

    }


    }

    @Override
    public void onDownloadStateChange(DownLoadInfo info) {
        refreshUIOnMainThread(info);
    }

    @Override
    public void onDownloadProgressChange(DownLoadInfo info) {
        refreshUIOnMainThread(info);
    }

    private void refreshUIOnMainThread(final DownLoadInfo info) {
        // 判断下载对象是否是当前应用
        AppDetail appInfo = getData();
        if (appInfo.id.equals(info.id)) {
            UIUtils.runOnUIThread(new Runnable() {

                @Override
                public void run() {
                    refreshUI(info.currentState, info.getProgress(), info.id);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_progress:
                // 根据当前状态来决定下一步操作
                if (mCurrentState == DownloadManager.STATE_UNDO
                        || mCurrentState == DownloadManager.STATE_FAILURE
                        || mCurrentState == DownloadManager.STATE_PAUSE) {
                    mDM.downLoad(getData());// 开始下载
                } else if (mCurrentState == DownloadManager.STATE_LOADING
                        || mCurrentState == DownloadManager.STATE_WAIT) {
                    mDM.pause(getData());// 暂停下载
                } else if (mCurrentState == DownloadManager.STATE_SUCCESS) {
                    mDM.install(getData());// 开始安装
                }

                break;

            default:
                break;
        }

    }
}
