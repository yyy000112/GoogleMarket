package android.ye.googlemarket.Holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.ye.googlemarket.Http.HttpHelper;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.BitmapHelper;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.domain.AppDetail;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by ye on 2016/11/13.
 */
public class AppDetailHolder extends BaseHolder<AppDetail>{

    private ImageView ivIcon;
    private TextView tvName;
    private TextView tvDownloadNum;
    private TextView tvVersion;
    private TextView tvDate;
    private TextView tvSize;
    private RatingBar rbStar;
    private BitmapUtils bitmapUtils;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_appinfo);

        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvDownloadNum = (TextView) view.findViewById(R.id.tv_download_num);
        tvVersion = (TextView) view.findViewById(R.id.tv_version);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvSize = (TextView) view.findViewById(R.id.tv_size);
        rbStar = (RatingBar) view.findViewById(R.id.rb_star);

        bitmapUtils = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(AppDetail data) {
        bitmapUtils.display(ivIcon, HttpHelper.URL + "image?name="
                + data.iconUrl);
        tvName.setText(data.name);
        tvDownloadNum.setText("下载量:" + data.downloadNum);
        tvVersion.setText("版本号:" + data.version);
        tvDate.setText(data.date);
        tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
        rbStar.setRating(data.stars);
    }
}
