package android.ye.googlemarket.Http.protocol;

import android.graphics.Picture;
import android.ye.googlemarket.Utils.LogUtils;
import android.ye.googlemarket.domain.HomeList0;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/10.
 */
public class HomeProtocol extends BaseProtocol<ArrayList<HomeList0.List>> {

    private ArrayList<String> pictures;

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public String getKey() {
        return "home";
    }

    @Override
    public ArrayList<HomeList0.List> parseData(String result) {
        Gson gson = new Gson();
        HomeList0 homeList0 = gson.fromJson(result, HomeList0.class);
        ArrayList<HomeList0.List> appinfos = homeList0.list;

        pictures = homeList0.picture;

        return appinfos;

     /*   try {
            JSONObject jo = new JSONObject(result);

            // 解析应用列表数据
            JSONArray ja = jo.getJSONArray("list");
            ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo1 = ja.getJSONObject(i);

                AppInfo info = new AppInfo();
                info.des = jo1.getString("des");
                info.downloadUrl = jo1.getString("downloadUrl");
                info.iconUrl = jo1.getString("iconUrl");
                info.id = jo1.getString("id");
                info.name = jo1.getString("name");
                info.packageName = jo1.getString("packageName");
                info.size = jo1.getLong("size");
                info.stars = (float) jo1.getDouble("stars");

                appInfoList.add(info);
            }

            // 初始化轮播条的数据
            JSONArray ja1 = jo.getJSONArray("picture");
            ArrayList<String> pictures = new ArrayList<String>();
            for (int i = 0; i < ja1.length(); i++) {
                String pic = ja1.getString(i);
                pictures.add(pic);
            }

            return appInfoList;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;*/
    }

    public ArrayList<String> getPictureList() {
        return pictures;
    }
}
