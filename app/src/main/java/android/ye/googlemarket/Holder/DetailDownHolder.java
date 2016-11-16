package android.ye.googlemarket.Holder;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.ye.googlemarket.Manager.DownloadManager;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.LogUtils;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.View.ProgressHorizontal;
import android.ye.googlemarket.domain.AppDetail;
import android.ye.googlemarket.domain.DownLoadInfo;

/**
 * Created by ye on 2016/11/15.
 */
public class DetailDownHolder extends BaseHolder<AppDetail> implements DownloadManager.DownloadObserver,View.OnClickListener{


    private Button btDownload;
    private FrameLayout flProgress;
    private ProgressHorizontal pbProgress;
    private DownloadManager dm;
    private int mCurrentState;
    private float mProgress;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_download);
        btDownload = (Button) view.findViewById(R.id.btn_download);
        btDownload.setOnClickListener(this);
        // 初始化自定义进度条
        flProgress = (FrameLayout) view.findViewById(R.id.fl_progress);
        flProgress.setOnClickListener(this);
        pbProgress = new ProgressHorizontal(UIUtils.getContext());
        pbProgress.setProgressBackgroundResource(R.mipmap.progress_bg);// 进度条背景图片
        pbProgress.setProgressResource(R.mipmap.progress_normal);// 进度条图片
        pbProgress.setProgressTextColor(Color.WHITE);// 进度文字颜色
        pbProgress.setProgressTextSize(UIUtils.dip2px(18));// 进度文字大小

        // 宽高填充父窗体
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        flProgress.addView(pbProgress, params);


        dm = DownloadManager.getInstance();
        // 注册观察者, 监听状态和进度变化
        dm.registerObserver(this);
        return view;
    }

    @Override
    public void refreshView(AppDetail data) {
        DownLoadInfo downLoadInfo = dm.getDownloadInfo(data);
        //判断是否下载过应用
        if (downLoadInfo != null){
            //之前下载过
            mCurrentState = downLoadInfo.currentState;
            mProgress = downLoadInfo.getProgress();
        }else {
            mCurrentState = DownloadManager.STATE_UNDO;
            mProgress = 0;
        }
        refreshUI(mCurrentState, mProgress);
    }

    //根据当前的下载进度和状态来更新界面
    private void refreshUI(int currentState, float progress) {
        mCurrentState = currentState;
        mProgress = progress;
        switch (currentState) {
            case DownloadManager.STATE_UNDO:// 未下载
                flProgress.setVisibility(View.GONE);
                btDownload.setVisibility(View.VISIBLE);
                btDownload.setText("下载");
                break;

            case DownloadManager.STATE_WAIT:// 等待下载
                flProgress.setVisibility(View.GONE);
                btDownload.setVisibility(View.VISIBLE);
                btDownload.setText("等待中..");
                break;

            case DownloadManager.STATE_LOADING:// 正在下载
                flProgress.setVisibility(View.VISIBLE);
                btDownload.setVisibility(View.GONE);
                pbProgress.setCenterText("");
                pbProgress.setProgress(mProgress);// 设置下载进度
                break;

            case DownloadManager.STATE_PAUSE:// 下载暂停
                flProgress.setVisibility(View.VISIBLE);
                btDownload.setVisibility(View.GONE);
                pbProgress.setCenterText("暂停");
                pbProgress.setProgress(mProgress);

                break;

            case DownloadManager.STATE_FAILURE:// 下载失败
                flProgress.setVisibility(View.GONE);
                btDownload.setVisibility(View.VISIBLE);
                btDownload.setText("下载失败");
                break;

            case DownloadManager.STATE_SUCCESS:// 下载成功
                flProgress.setVisibility(View.GONE);
                btDownload.setVisibility(View.VISIBLE);
                btDownload.setText("安装");
                break;

            default:
                break;
        }

    }

    //使refreshUI方法在主线程更新
    private void refreshUIOnMainThread(final DownLoadInfo info) {
        AppDetail appDetail = getData();
        // 判断下载对象是否是当前应用
        if (appDetail.id.equals(info.id)){
            UIUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {

                    //refreshUI(mCurrentState,mProgress);这样会出问题
                    refreshUI(info.currentState, info.getProgress());
                }
            });
        }
    }
    //状态更新
    @Override
    public void onDownloadStateChange(DownLoadInfo info) {
        refreshUIOnMainThread(info);
    }



    @Override
    public void onDownloadProgressChange(DownLoadInfo info) {

        refreshUIOnMainThread(info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
            case R.id.fl_progress:

                // 根据当前状态来决定下一步操作
                if (mCurrentState == DownloadManager.STATE_UNDO
                        || mCurrentState == DownloadManager.STATE_FAILURE
                        || mCurrentState == DownloadManager.STATE_PAUSE) {
                    dm.downLoad(getData());// 开始下载
                } else if (mCurrentState == DownloadManager.STATE_LOADING
                        || mCurrentState == DownloadManager.STATE_WAIT) {
                    dm.pause(getData());// 暂停下载
                } else if (mCurrentState == DownloadManager.STATE_SUCCESS) {
                    dm.install(getData());// 开始安装
                }

                break;

            default:
                break;
        }
    }
}
