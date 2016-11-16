package android.ye.googlemarket.Http.protocol;



import android.ye.googlemarket.domain.AppDetail;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/10.
 */
//public class HomeProtocol extends BaseProtocol<ArrayList<HomeList0.List>> 写到最后需要传一个对象而不是数组，重写
public class HomeProtocol extends BaseProtocol<ArrayList<AppDetail>> {


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
    public ArrayList<AppDetail> parseData(String result) {
        /*Gson gson = new Gson();
        HomeList0 homeList0 = gson.fromJson(result, HomeList0.class);
        ArrayList<HomeList0.List> appinfos = homeList0.list;

        pictures = homeList0.picture;

        return appinfos;*/

        try {
            JSONObject jo = new JSONObject(result);

            // 解析应用列表数据
            JSONArray ja = jo.getJSONArray("list");
            ArrayList<AppDetail> appInfoList = new ArrayList<AppDetail>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo1 = ja.getJSONObject(i);

                AppDetail info = new AppDetail();
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
            pictures = new ArrayList<String>();
            for (int i = 0; i < ja1.length(); i++) {
                String pic = ja1.getString(i);
                pictures.add(pic);
            }

            return appInfoList;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getPictureList() {
        return pictures;
    }
}
