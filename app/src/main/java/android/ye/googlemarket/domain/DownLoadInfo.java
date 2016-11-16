package android.ye.googlemarket.domain;

import android.os.Environment;
import android.ye.googlemarket.Manager.DownloadManager;

import java.io.BufferedReader;
import java.io.File;

/**
 * 下载对象
 * Created by ye on 2016/11/15.
 */
public class DownLoadInfo {
    public String id;
    public String name;
    public String downloadUrl;
    public long size;
    public String packageName;

    public long currentPos;// 当前下载位置
    public int currentState;// 当前下载状态
    public String path;// 下载到本地文件的路径

    public static final String GOOGLE_MARKET = "GOOGLE_MARKET";// sdcard根目录文件夹名称
    public static final String DONWLOAD = "download";// 子文件夹名称, 存放下载的文件

    //获取下载进度
    public float getProgress(){
        if (size == 0){
            return 0;
        }
        float progress = (float)currentPos/size;
        return progress;
    }

    //获取下载路径
    public String getFilePath(){
        StringBuffer sb = new StringBuffer();
       String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        sb.append(sd);
        sb.append(File.separator);
        sb.append(GOOGLE_MARKET);
        sb.append(File.separator);
        sb.append(DONWLOAD);

        if (createDir(sb.toString())){
            // 文件夹存在或者已经创建完成
            //返回文件路径
            return  sb.toString()+File.separator+name+".apk";
        }
        return null;
    }

    //从AppDetail中拷贝对象，并获取下载路径
    public static DownLoadInfo copy(AppDetail info){
        DownLoadInfo downLoadInfo = new DownLoadInfo();
        downLoadInfo.id = info.id;
        downLoadInfo.name = info.name;
        downLoadInfo.size = info.size;
        downLoadInfo.downloadUrl = info.downloadUrl;
        downLoadInfo.packageName = info.packageName;
        downLoadInfo.path = downLoadInfo.getFilePath();
        //默认当前下载位置为0
        downLoadInfo.currentPos = 0;
        //默认下载状态为未下载
        downLoadInfo.currentState = DownloadManager.STATE_UNDO;


        return downLoadInfo;
    }

    //判断文件夹是否创建或存在
    private boolean createDir(String dir){
        File dirFile = new File(dir);
        // 文件夹不存在或者不是一个文件夹
        if (!dirFile.exists()||!dirFile.isDirectory()){
            return dirFile.mkdirs();
        }
        return true;
    }
}
