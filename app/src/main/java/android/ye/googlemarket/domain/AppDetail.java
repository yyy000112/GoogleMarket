package android.ye.googlemarket.domain;

import java.util.ArrayList;

/**
 * 详情页
 * Created by ye on 2016/11/13.
 */
public class AppDetail {
    public ArrayList<SafeInfo> safe;
    public ArrayList<String> screen;
    public ArrayList<String> picture;


    public String des;
    public String downloadUrl;
    public String iconUrl;
    public String id;
    public String name;
    public String packageName;
    public long size;
    public float stars;
    public String author;
    public String date;
    public String downloadNum;
    public String version;


    public class SafeInfo {
        public String safeDes;
        public String safeDesUrl;
        public String safeUrl;
    }
}
