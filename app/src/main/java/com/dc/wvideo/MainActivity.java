package com.dc.wvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dc.wvideo.databinding.ActivityMainBinding;
import com.dc.wvideo.utils.fileUriUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import java.util.List;

import cn.yanjingtp.utils.utils.FileUtilsKt;
import cn.yanjingtp.utils.utils.SAFUtilsKt;

public class MainActivity extends AppCompatActivity {

private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityMainBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
//        boolean ret = SAFUtilsKt.getSAFGrant("Android/data/com.tencent.mm/MicroMsg/Download");
//        Log.d("wdc", "onCreate: " + ret);
        PermissionX.init(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            Toast.makeText(MainActivity.this, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        fileUriUtils.startFor("/sdcard/Android/data", this, 1);
    }

    /**
     * A native method that is implemented by the 'wvideo' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'wvideo' library on application startup.
    static {
        System.loadLibrary("wvideo");
    }

}