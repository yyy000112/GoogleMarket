package android.ye.googlemarket.Holder;

import android.view.View;
import android.widget.ImageView;
import android.ye.googlemarket.Http.HttpHelper;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.BitmapHelper;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.domain.AppDetail;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/14.
 */
public class HorizontalHolder extends BaseHolder<AppDetail> {

    private ImageView[] images;
    private BitmapUtils bitmapUtils;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_scroll_pic);
        images = new ImageView[5];
        images[0] = (ImageView) view.findViewById(R.id.iv_pic1);
        images[1] = (ImageView) view.findViewById(R.id.iv_pic2);
        images[2] = (ImageView) view.findViewById(R.id.iv_pic3);
        images[3] = (ImageView) view.findViewById(R.id.iv_pic4);
        images[4] = (ImageView) view.findViewById(R.id.iv_pic5);
        bitmapUtils = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(AppDetail data) {
        ArrayList<String> screens = data.screen;
        for (int i = 0; i<5;i++){
            if (i<screens.size()){
                String screen = screens.get(i);
                bitmapUtils.display(images[i], HttpHelper.URL
                        + "image?name=" +screen);
            }else {
                images[i].setVisibility(View.GONE);
            }
        }

    }
}
