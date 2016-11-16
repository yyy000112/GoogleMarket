package android.ye.googlemarket.Activity;


import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.ye.googlemarket.PagerTab;
import android.ye.googlemarket.Permit.PermissionCallBack;
import android.ye.googlemarket.Permit.PermissionManager;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.LogUtils;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.fragment.BaseFragment;
import android.ye.googlemarket.fragment.FragmentFactory;

public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private PagerTab mPagerTab;
    private String[] tabNames;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initActionBar();

        requirePermit();
    }


    private void initActionBar() {
        ActionBar actionBar =getSupportActionBar();
        // home处可以点击
        actionBar.setHomeButtonEnabled(true);
        // 显示左上角返回键,当和侧边栏结合时展示三个杠图片
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.dl_drawer);
        //初始化开关
        toggle = new ActionBarDrawerToggle(this,drawer, R.string.drawer_open,R.string.drawer_close);
        //设置开关与状态同步
        toggle.syncState();

    }

    //拦截actionbar的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                toggle.onOptionsItemSelected(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void initView() {
        mPagerTab = (PagerTab) findViewById(R.id.pager_tab);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());
        String path0 = Environment.getExternalStorageDirectory().getAbsolutePath();
        //给ViewPager设置适配器
        mViewPager.setAdapter(mAdapter);
        //将viewPager设置给指示器
        mPagerTab.setViewPager(mViewPager);

        mPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = FragmentFactory.createFragment(position);
               //开始加载数据
                fragment.loadData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //设置fragmentAdapter
    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
            //标题数组
            tabNames = UIUtils.getStringArray(R.array.tab_names);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }

        @Override
        public Fragment getItem(int position) {
          BaseFragment fragment =  FragmentFactory.createFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }
    }



    private void requirePermit() {
        if(PermissionManager.getInstance().hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //已经获取权限
        }
        else{
            if(PermissionManager.getInstance().shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //已经询问过权限但是被拒绝，在这里一般显示一些为什么要需要权限，提示用户去设置里激活权限
                UIUtils.ToastShow("需激活权限，否则某些功能无法正常使用");

            }
            else{
                PermissionManager.getInstance().requestPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        new PermissionCallBack() {
                            @Override
                            public void onGranted(String[] permissions, int[] grantResults) {
                                //获得权限成功
                            }

                            @Override
                            public void onFailed(String[] permissions, int[] grantResults) {
                                //获得权限失败
                                //permission数组与grantResults数组位置想对应，可以看到具体每个权限是否被获取
                            }
                        });
            }
        }

        if(PermissionManager.getInstance().hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            //已经获取权限
        }
        else{
            if(PermissionManager.getInstance().shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                //已经询问过权限但是被拒绝，在这里一般显示一些为什么要需要权限，提示用户去设置里激活权限
                UIUtils.ToastShow("需激活权限，否则某些功能无法正常使用");

            }
            else{
                PermissionManager.getInstance().requestPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        new PermissionCallBack() {
                            @Override
                            public void onGranted(String[] permissions, int[] grantResults) {
                                //获得权限成功
                            }

                            @Override
                            public void onFailed(String[] permissions, int[] grantResults) {
                                //获得权限失败
                                //permission数组与grantResults数组位置想对应，可以看到具体每个权限是否被获取
                            }
                        });
            }
        }
    }

}
