package android.ye.googlemarket.Manager;

import android.content.Intent;
import android.net.Uri;
import android.renderscript.ScriptGroup;
import android.ye.googlemarket.Http.HttpHelper;
import android.ye.googlemarket.Utils.IOUtils;
import android.ye.googlemarket.Utils.LogUtils;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.domain.AppDetail;
import android.ye.googlemarket.domain.DownLoadInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 下载管理
 * 下载状态：未下载 - 等待下载 - 正在下载 - 暂停下载 - 下载失败 - 下载成功
 * Created by ye on 2016/11/15.
 */
public class DownloadManager {

    public static final int STATE_UNDO = 0;
    public static final int STATE_WAIT = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_PAUSE = 3;
    public static final int STATE_FAILURE = 4;
    public static final int STATE_SUCCESS = 5;

    private static DownloadManager dm = new DownloadManager();

    //建立观察者集合
    private ArrayList<DownloadObserver> mObserver = new ArrayList<DownloadObserver>();
    //下载对象的集合
    private HashMap<String, DownLoadInfo> mDownload = new HashMap<String,DownLoadInfo>();

    //下载任务的集合
    private HashMap<String, DownloadTask> mDownloadTask = new HashMap<String,DownloadTask>();;

    private DownloadManager(){

    }

    public static DownloadManager getInstance(){
        return dm;
    }

    /**
     * 声明观察者接口
     */
    public interface DownloadObserver {
        //状态发生变化
        public void onDownloadStateChange(DownLoadInfo info);

        //下载进度变化
        public void onDownloadProgressChange(DownLoadInfo info);
    }

    //注册观察者
    public void registerObserver(DownloadObserver observer){
        if (observer!=null && !mObserver.contains(observer)){
            mObserver.add(observer);
        }
    }

    //注销观察者
    public void unregisterObserver(DownloadObserver observer){
        if (observer!=null && mObserver.contains(observer)){
            mObserver.remove(observer);
        }
    }

    //通知下载状态发生改变
    public void notifyDownloadStateChanged(DownLoadInfo info){
        for(DownloadObserver observer:mObserver){
            observer.onDownloadStateChange(info);
        }
    }

    //通知下载进度发生改变
    public void notifyDownloadProgressChanged(DownLoadInfo info){
        for(DownloadObserver observer:mObserver){
            observer.onDownloadProgressChange(info);
        }
    }

    //开始下载
    public void downLoad(AppDetail info){

        // 如果对象是第一次下载, 需要创建一个新的DownloadInfo对象,从头下载,如果之前下载过, 要接着下载,实现断点续传
        DownLoadInfo downLoadInfo = mDownload.get(info.id);
        if (downLoadInfo == null){
            //生成下载对象
             downLoadInfo = DownLoadInfo.copy(info);
        }
        //状态更改为等待下载，因为可能还在线程排队中
        downLoadInfo.currentState = STATE_WAIT;
        //通知所有观察者状态发生改变
        notifyDownloadStateChanged(downLoadInfo);

        //将下载对象保存在集合中
        mDownload.put(downLoadInfo.id,downLoadInfo);
        //创建一个任务对象
        DownloadTask task = new DownloadTask(downLoadInfo);
        ThreadManager.getThreadPool().execute(task);
        // 将下载任务放入集合中
        mDownloadTask.put(downLoadInfo.id,task);
    }

    //下载任务对象
    class DownloadTask implements Runnable{
        private DownLoadInfo downLoadInfo;
        private HttpHelper.HttpResult httpResult;

        public DownloadTask(DownLoadInfo downLoadInfo){
            this.downLoadInfo = downLoadInfo;
        }

        @Override
        public void run() {

            downLoadInfo.currentState = STATE_LOADING;
            notifyDownloadStateChanged(downLoadInfo);

            File file = new File(downLoadInfo.path);
            if (!file.exists() || file.length()!=downLoadInfo.currentPos || downLoadInfo.currentPos == 0){
                // 从头开始下载,删除无效文件
                file.delete();
                //重置当前下载位置为0
                downLoadInfo.currentPos = 0;
                //重头开始下载
                httpResult = HttpHelper.download(HttpHelper.URL + "download?name=" + downLoadInfo.downloadUrl);
            }else{
                //断点续传，range 表示请求服务器从文件的哪个位置开始返回数据
                httpResult = HttpHelper.download(HttpHelper.URL + "download?name=" + downLoadInfo.downloadUrl
                        + "&range=" + file.length());
            }
            if (httpResult != null && httpResult.getInputStream()!=null){
                InputStream in = httpResult.getInputStream();
                FileOutputStream out = null;

                try {
                    //表示在原来文件上追加数据
                    out = new FileOutputStream(file,true);
                    int length = 0;
                    byte[] buffer = new byte[1024];

                    //只有在正确状态以及读取行数不为空的状态下继续循环，解决下载过程中暂停的问题
                    while ((length =in.read(buffer))!=-1 && downLoadInfo.currentState == STATE_LOADING){
                        out.write(buffer,0,length);
                        out.flush();
                        //更新下载进度
                        downLoadInfo.currentPos +=length;
                        //通知下载进度更新
                        notifyDownloadProgressChanged(downLoadInfo);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    IOUtils.close(in);
                    IOUtils.close(out);
                }
                //文件完整表示下载结束
                if (file.length() == downLoadInfo.size){
                    downLoadInfo.currentState = STATE_SUCCESS;
                    //通知状态更新
                    notifyDownloadStateChanged(downLoadInfo);
            }else if (downLoadInfo.currentState == STATE_PAUSE){
                    //中途暂停
                    notifyDownloadStateChanged(downLoadInfo);
                }else {
                    //下载失败
                    file.delete();//删除无效文件
                    downLoadInfo.currentState =STATE_FAILURE;
                    downLoadInfo.currentPos = 0;
                    notifyDownloadStateChanged(downLoadInfo);
                }

                //从集合中移除任务
                mDownloadTask.remove(downLoadInfo.id);

            }
        }
    }

    //暂停下载
    public void pause(AppDetail info){
        //获取下载对象
        DownLoadInfo downLoadInfo = mDownload.get(info.id);
        if (downLoadInfo != null){
            // 只有在正在下载和等待下载时才需要暂停
            if (downLoadInfo.currentState == STATE_LOADING || downLoadInfo.currentState == STATE_WAIT){
                DownloadTask task = mDownloadTask.get(downLoadInfo.id);
                if (task != null){
                    //移除下载任务，如果任务还没开始,正在等待, 可以通过此方法移除
                    // 如果任务已经开始运行, 需要在run方法里面进行中断
                    ThreadManager.getThreadPool().cancle(task);
                }
                //切换下载状态为暂停
                downLoadInfo.currentState = STATE_PAUSE;
                notifyDownloadStateChanged(downLoadInfo);
            }
        }
    }


    //安装
    public void install(AppDetail info){
        DownLoadInfo downLoadInfo = mDownload.get(info.id);
        if (downLoadInfo!=null){
            // 跳到系统的安装页面进行安装
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + downLoadInfo.path),
                    "application/vnd.android.package-archive");
            UIUtils.getContext().startActivity(intent);
        }
    }

    // 根据应用信息返回下载对象
    public DownLoadInfo getDownloadInfo(AppDetail info){
        return mDownload.get(info.id);
    }

}
