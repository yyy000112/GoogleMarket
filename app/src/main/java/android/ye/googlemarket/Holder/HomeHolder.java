package android.ye.googlemarket.Holder;

import android.app.ActivityManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.ye.googlemarket.Http.HttpHelper;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.BitmapHelper;
import android.ye.googlemarket.Utils.LogUtils;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.domain.AppInfo;
import android.ye.googlemarket.domain.HomeList0;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by ye on 2016/11/10.
 */
public class HomeHolder extends BaseHolder<HomeList0.List> {

    private View view;
    private ImageView ivIcon;
    private TextView tvName;
    private RatingBar rbStar;
    private TextView tvSize;
    private TextView tvDes;
    private BitmapUtils mBitmapUtils;


    @Override
    public View initView() {
        view = UIUtils.inflate(R.layout.list_item_home);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        rbStar = (RatingBar) view.findViewById(R.id.rb_star);
        tvSize = (TextView) view.findViewById(R.id.tv_size);
        tvDes = (TextView) view.findViewById(R.id.tv_des);
        mBitmapUtils = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(HomeList0.List data) {
        tvName.setText(data.name);
        rbStar.setRating(data.stars);
        tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
        tvDes.setText(data.des);
        mBitmapUtils.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);
    }

}
