package android.ye.googlemarket.Activity;


import android.support.v7.app.AppCompatActivity;
import android.ye.googlemarket.Permit.PermissionManager;

/**
 * Created by ye on 2016/11/9.
 */
public class BaseActivity extends AppCompatActivity {

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionManager.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

}

