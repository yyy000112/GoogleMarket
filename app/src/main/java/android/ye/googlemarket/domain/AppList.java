package android.ye.googlemarket.domain;

import android.graphics.Picture;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/10.
 */
public class AppList {
    public ArrayList<List> list;

    public class List{
        public String id;
        public String name;
        public String packageName;
        public String iconUrl;
        public float stars;
        public long size;
        public String downloadUrl;
        public String des;

    }
}
