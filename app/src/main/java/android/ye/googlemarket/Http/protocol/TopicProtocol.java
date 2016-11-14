package android.ye.googlemarket.Http.protocol;

import android.ye.googlemarket.domain.TopicInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/11.
 */
public class TopicProtocol extends BaseProtocol<ArrayList<TopicInfo>> {
    @Override
    public String getParams() {
        return "";
    }

    @Override
    public String getKey() {
        return "subject";
    }

    @Override
    public ArrayList<TopicInfo> parseData(String result) {
        try {
            JSONArray ja = new JSONArray(result);
            ArrayList<TopicInfo> list = new ArrayList<TopicInfo>();

            for (int i = 0; i<ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);

                TopicInfo info = new TopicInfo();
                info.des = jo.getString("des");
                info.url = jo.getString("url");
                list.add(info);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
