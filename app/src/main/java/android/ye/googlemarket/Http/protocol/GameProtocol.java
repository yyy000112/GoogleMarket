package android.ye.googlemarket.Http.protocol;

import android.ye.googlemarket.domain.AppInfo;
import android.ye.googlemarket.domain.GameInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/11.
 */
public class GameProtocol extends BaseProtocol<ArrayList<GameInfo>> {
    @Override
    public String getParams() {
        return "";
    }

    @Override
    public String getKey() {
        return "game";
    }

    @Override
    public ArrayList<GameInfo> parseData(String result) {
        try {
            JSONArray ja = new JSONArray(result);

            ArrayList<GameInfo> list = new ArrayList<GameInfo>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                GameInfo info = new GameInfo();
                info.des = jo.getString("des");
                info.downloadUrl = jo.getString("downloadUrl");
                info.iconUrl = jo.getString("iconUrl");
                info.id = jo.getString("id");
                info.name = jo.getString("name");
                info.packageName = jo.getString("packageName");
                info.size = jo.getLong("size");
                info.stars = (float) jo.getDouble("stars");

                list.add(info);
            }

            return list;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
