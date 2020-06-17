package com.cbat.cbat.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cbat.cbat.R;
import com.cbat.cbat.util.PermissionHelper;

import java.util.List;
import java.util.logging.Logger;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks { //
    
    private static final String TAG = "SplashActivity";
    
    protected static final int ACTIVITY_DELAY_TIME = 1500;// 毫秒
    protected static final int ACTIVITY_TO_LOAD = 1;
    
    private static final int REQUEST_CODE_PERMISSION = 0x8141;
    
    public static final String EXTRA_SHOW_SETTINGS_DIALOG = "SplashActivity:showSettingsDialog";
    
    private boolean mNeedsShowSettingsDialog = false;
    private ProgressDialog progressDialog;
    private AnimationDrawable connectingAnimationDrawable;
    private Drawable connectedDrawable;
    private Drawable disconnectedDrawable;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {



            if (msg.what == ACTIVITY_TO_LOAD) {
                //SharedPreferences shp = getSharedPreferences(BHUserInfo.SHP_NAME_USERINFO, Context
                 //       .MODE_PRIVATE);

                /* 不写入设备的数据 */
               // String  loginId = shp.getString(BHUserInfo.USERINFO_KEY_LOGIN_EMAIL,"");
               // String  password = shp.getString(BHUserInfo.USERINFO_KEY_PASSWORD, "");
                //Log.d("userID",loginId);
                //Log.d("password",password);

//                if(loginId!=null && !loginId.isEmpty() && password!=null && !password.isEmpty()){
//                    Intent intent = new Intent();
//                    intent.setClass(getBaseContext(), MainActivity.class);
//                    intent.putExtra(EXTRA_SHOW_SETTINGS_DIALOG, mNeedsShowSettingsDialog);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//                    SplashActivity.this.finish();
//
//                }else {
                    Intent intent = new Intent();
                    // intent.setClass(getBaseContext(), MainActivity.class);
                    intent.setClass(getBaseContext(), LoginActivity.class);
                    intent.putExtra(EXTRA_SHOW_SETTINGS_DIALOG, mNeedsShowSettingsDialog);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    SplashActivity.this.finish();
               // }
                if(progressDialog!=null) {
                    progressDialog.dismiss();
                }
            }
            return true;
        }
    });
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);
        progressDialog = new ProgressDialog(SplashActivity.this,
                R.style.AppTheme_Dark_Dialog);
        // 申请权限
        requestPermission();

        // mHandler.sendEmptyMessageDelayed(ACTIVITY_TO_LOAD, ACTIVITY_DELAY_TIME);
    }
    
    private void gotoMain() {
        mHandler.sendEmptyMessage(ACTIVITY_TO_LOAD);
//        if (progressDialog != null ) {
//            initDialog(true);
//            this.updateConnectState(BHUartBinder.getInstance().getUartConnectionState());
//
//        }


    }

    private void requestPermission(){
        gotoMain();
        if (!EasyPermissions.hasPermissions(this, PermissionHelper.Needs_Permissions)){
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_ask),
                   REQUEST_CODE_PERMISSION, PermissionHelper.Needs_Permissions);
//            EasyPermissions.requestPermissions(this,null,
//                  REQUEST_CODE_PERMISSION, PermissionHelper.Needs_Permissions);
        } else {
            gotoMain();
            // mHandler.sendEmptyMessageDelayed(ACTIVITY_TO_LOAD, ACTIVITY_DELAY_TIME);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if(!mNeedsShowSettingsDialog) {
            gotoMain();

        }
    }

    /// MARK: EasyPermissions

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
      //  Logger.d(TAG, "onPermissionsGranted: ");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.w(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        for (String perm : perms) {
            Log.w(TAG, "onPermissionsDenied: " + perm);
        }
    
        // new AppSettingsDialog.Builder(this).build().show();

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            mNeedsShowSettingsDialog = true;
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Logger.d(TAG, "onActivityResult: ");
        gotoMain();
    }
}
