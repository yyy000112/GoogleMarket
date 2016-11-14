package android.ye.googlemarket.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.ye.googlemarket.Http.HttpHelper;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.BitmapHelper;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.domain.TopicInfo;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/11.
 */
public class TopicHolder extends BaseHolder<TopicInfo> {

    private TextView tvDesc;
    private ImageView ivPic;
    private BitmapUtils mBitmapUtils;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_topic);
        ivPic = (ImageView) view.findViewById(R.id.iv_pic);
        tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        mBitmapUtils = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(TopicInfo data) {
        tvDesc.setText(data.des);
        mBitmapUtils.display(ivPic, HttpHelper.URL + "image?name="+data.url);

    }
}
