package android.ye.googlemarket.Http.protocol;

import android.ye.googlemarket.domain.AppDetail;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/13.
 */
public class HomeDetailProtocol extends BaseProtocol<AppDetail> {

    public String packageName;
    private ArrayList<AppDetail.SafeInfo> safeInfos;

    public HomeDetailProtocol(String packageName){
        this.packageName = packageName;
    }
    @Override
    public String getParams() {
        return "&packageName=" + packageName;
    }

    @Override
    public String getKey() {
        return "detail";
    }

    @Override
    public AppDetail parseData(String result) {
        Gson gson = new Gson();
        AppDetail appDetail = gson.fromJson(result, AppDetail.class);
        safeInfos = appDetail.safe;
        return appDetail;
    }

}
