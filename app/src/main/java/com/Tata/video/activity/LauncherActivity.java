package com.Tata.video.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.Tata.video.AppConfig;
import com.Tata.video.R;
import com.Tata.video.bean.UserBean;
import com.Tata.video.http.CheckTokenCallback;
import com.Tata.video.http.HttpUtil;
import com.Tata.video.utils.SharedPreferencesUtil;

/**
 * 启动页面
 */
public class LauncherActivity extends AbsActivity {

    private Handler mHandler;
    private   boolean advertisment_state=true;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void main() {
        mHandler = new Handler();
        this.advertisment_state= getAdvertismentState() ;
        SharedPreferencesUtil.getInstance().saveAdvertismentState(advertisment_state);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppConfig.getInstance().isLogin()) {
                    //检查Token是否过期
                    HttpUtil.ifToken(new CheckTokenCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            if (code == 0 && info.length > 0) {
                                UserBean userBean = JSON.parseObject(info[0], UserBean.class);
                                if (userBean != null) {
                                    SharedPreferencesUtil.getInstance().saveUserBeanJson(info[0]);
                                    AppConfig.getInstance().setUserBean(userBean);
                                    forwardMainActivity();
                                }
                            }else if (code == 700) {
                                AppConfig.getInstance().logout();
                                AppConfig.getInstance().logoutJPush();
                                forwardMainActivity();
                            }
                        }
                    });
                } else {
                    forwardMainActivity();
                }
            }
        }, 500);
    }


    private void forwardMainActivity() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    public  Boolean getAdvertismentState(){
        return  advertisment_state;
    }


    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpUtil.GET_CONFIG);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }
}
