package android.ye.googlemarket.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.ye.googlemarket.Http.HttpHelper;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.BitmapHelper;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.domain.CategoryInfo;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by ye on 2016/11/13.
 */
public class CategoryHolder extends BaseHolder<CategoryInfo> implements View.OnClickListener {

    private LinearLayout llGrid1,llGrid2,llGrid3;
    private ImageView ivIcon1,ivIcon2,ivIcon3;
    private TextView tvName1,tvName2,tvName3;
    private BitmapUtils bitmapUtils;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_category);

        llGrid1 = (LinearLayout) view.findViewById(R.id.ll_grid1);
        llGrid2 = (LinearLayout) view.findViewById(R.id.ll_grid2);
        llGrid3 = (LinearLayout) view.findViewById(R.id.ll_grid3);

        ivIcon1 = (ImageView) view.findViewById(R.id.iv_icon1);
        ivIcon2 = (ImageView) view.findViewById(R.id.iv_icon2);
        ivIcon3 = (ImageView) view.findViewById(R.id.iv_icon3);

        tvName1 = (TextView) view.findViewById(R.id.tv_name1);
        tvName2 = (TextView) view.findViewById(R.id.tv_name2);
        tvName3 = (TextView) view.findViewById(R.id.tv_name3);
        bitmapUtils = BitmapHelper.getBitmapUtils();

        llGrid1.setOnClickListener(this);
        llGrid2.setOnClickListener(this);
        llGrid3.setOnClickListener(this);
        return view;
    }

    @Override
    public void refreshView(CategoryInfo data) {
        bitmapUtils.display(ivIcon1, HttpHelper.URL + "image?name="+data.url1);
        bitmapUtils.display(ivIcon2, HttpHelper.URL + "image?name="+data.url2);
        bitmapUtils.display(ivIcon3, HttpHelper.URL + "image?name="+data.url3);

        tvName1.setText(data.name1);
        tvName2.setText(data.name3);
        tvName3.setText(data.name2);
    }

    @Override
    public void onClick(View v) {
        CategoryInfo data = getData();
        switch (v.getId()){
            case R.id.ll_grid1:
                UIUtils.ToastShow(data.name1);
                break;
            case R.id.ll_grid2:
                UIUtils.ToastShow(data.name2);
                break;
            case R.id.ll_grid3:
                UIUtils.ToastShow(data.name3);
                break;
        }
    }
}
