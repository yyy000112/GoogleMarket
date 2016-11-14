package android.ye.googlemarket.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.UIUtils;

/**
 * 根据不同状态显示不同页面的自定义控件
 * Created by ye on 2016/11/9.
 */
public abstract class LoadingPager extends FrameLayout {

    //未加载
    private static int STATE_LOAD_UNDO = 1;
    //正在加载
    private static int STATE_LOADING = 2;
    //加载失败
    private static int STATE_LOAD_ERROR = 3;
    //数据为空
    private static int STATE_LOAD_EMPTY = 4;
    //加载成功
    private static int STATE_LOAD_SUCCESS = 5;

    //当前状态
    private int mCurrentStates = STATE_LOAD_ERROR;
    private View mLoadPage;
    private View mErrorPage;
    private View mEmptyPage;
    private View mSuccessView;

    public LoadingPager(Context context) {
        super(context);
        initView();
    }

    public LoadingPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadingPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public LoadingPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {

        if (mLoadPage == null){
            mLoadPage = UIUtils.inflate(R.layout.page_loading);
            addView(mLoadPage);
        }
        //未加载以及加载失败
        if (mErrorPage==null){
            mErrorPage = UIUtils.inflate(R.layout.page_error);
            Button btnLoadError = (Button) mErrorPage.findViewById(R.id.btn_load_error);
            btnLoadError.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadData();
                }
            });
            addView(mErrorPage);
        }

        //数据为空
        if (mEmptyPage==null){
            mEmptyPage = UIUtils.inflate(R.layout.page_empty);
            addView(mEmptyPage);
        }

        showRightPage();

    }

    private void showRightPage() {
        mLoadPage.setVisibility((mCurrentStates == STATE_LOADING || mCurrentStates == STATE_LOAD_UNDO) ? View.VISIBLE : View.GONE);
        mEmptyPage.setVisibility(mCurrentStates==STATE_LOAD_EMPTY ? View.VISIBLE:View.GONE);
        mErrorPage.setVisibility(mCurrentStates == STATE_LOAD_ERROR ? View.VISIBLE : View.GONE);

        if (mSuccessView == null && mCurrentStates == STATE_LOAD_SUCCESS){
            mSuccessView = onCreateSuccessView();
            if (mSuccessView != null){
                addView(mSuccessView);
            }
        }
        if (mSuccessView != null){
            mSuccessView.setVisibility(mCurrentStates == STATE_LOAD_SUCCESS ? View.VISIBLE : View.GONE);
        }
    }

    public void loadData(){
        if (mCurrentStates!=STATE_LOADING){
            mCurrentStates = STATE_LOADING;
            new Thread(){
                @Override
                public void run() {
                  final ResultState resultState =  onLoad();
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (resultState!=null){
                                //获取当前网络结束后的状态
                                mCurrentStates = resultState.getState();
                                //更新View
                                showRightPage();
                            }
                        }
                    });

                }
            }.start();
        }

    }

    public enum ResultState{
        STATE_SUCCESS(STATE_LOAD_SUCCESS), STATE_EMPTY(STATE_LOAD_EMPTY), STATE_ERROR(STATE_LOAD_ERROR);
        private int state;
        //创建构造方法
        private ResultState(int state){
            this.state = state;
        }
        public int getState(){
            return state;
        }
    }

    //加载成功的布局由子类来实现
    public abstract View onCreateSuccessView();
    //加载网络数据结束，返回请求网络结束后的状态
    public abstract ResultState onLoad();
}
