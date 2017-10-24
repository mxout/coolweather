package com.coolweather.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout container;
    private boolean canJump;//用于判断是否可以跳过广告，进入MainActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        container = (RelativeLayout)findViewById(R.id.container);

        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String [permissionList.size()]);
            ActivityCompat.requestPermissions(this,permissions,1);
        }else {
            requestAds();
        }
    }

    /*
     *请求开展广告
     */
    private void requestAds(){
        String appId = "1106496416";
        String adId = "8070225619064121";

        new SplashAD(this, container, appId, adId, new SplashADListener() {
            @Override
            public void onADDismissed() {
                forward();//广告显示完毕
            }

            @Override
            public void onNoAD(AdError adError) {
                Toast.makeText(getApplicationContext(),"loading ad failed",Toast.LENGTH_SHORT).show();
                forward();//广告加载失败
            }

            @Override
            public void onADPresent() {
                //广告加载成功
            }

            @Override
            public void onADClicked() {
                //广告被点击
            }

            @Override
            public void onADTick(long l) {

            }
        });
    }

    private void forward(){
        if(canJump){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            canJump = true;
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume(){
        super.onResume();
        canJump = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String [] permissions,int[]grantResult){
        switch (requestCode){
            case 1:
                if(grantResult.length > 0){
                    for(int result : grantResult){
                        if(result != PackageManager.PERMISSION_DENIED){
                            Toast.makeText(this,"you can't use the app if you don't grant the permissions",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestAds();
                }else {
                    Toast.makeText(this,"unknown error",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            default:
                break;
        }
    }
}
