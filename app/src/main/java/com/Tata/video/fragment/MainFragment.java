package com.Tata.video.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.Tata.video.AppConfig;
import com.Tata.video.Constants;
import com.Tata.video.R;
import com.Tata.video.activity.LoginActivity;
import com.Tata.video.activity.MainActivity;
import com.Tata.video.custom.DrawableRadioButton;
import com.Tata.video.jpush.JMessageUtil;
import com.Tata.video.utils.DpUtil;

/**
 * Created by cxf on 2018/6/5.
 */

public class MainFragment extends AbsFragment implements View.OnClickListener {

    private static final int HOME = 0;
    private static final int FOLLOW = 1;
    private static final int MSG = 2;
    private static final int ME = 3;

    private DrawableRadioButton mBtnHome;
    private DrawableRadioButton mBtnFollow;
    private DrawableRadioButton mBtnMsg;
    private DrawableRadioButton mBtnMe;
    private View mRecordTip;
    private Animation mAnimation;

    private int mCurKey;//当前选中的fragment的key
    private SparseArray<Fragment> mSparseArray;
    private FragmentManager mFragmentManager;
    private HomeFragment mHomeFragment;
    private boolean mLogout;
    private boolean mShowingRecordTip;
    private TextView mRedPoint;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void main() {
        mBtnHome = (DrawableRadioButton) mRootView.findViewById(R.id.btn_home);
        mBtnFollow = (DrawableRadioButton) mRootView.findViewById(R.id.btn_follow);
        mBtnMsg = (DrawableRadioButton) mRootView.findViewById(R.id.btn_msg);
        mBtnMe = (DrawableRadioButton) mRootView.findViewById(R.id.btn_me);
        mRedPoint = (TextView) mRootView.findViewById(R.id.red_point);
        mBtnHome.setOnClickListener(this);
        mBtnFollow.setOnClickListener(this);
        mBtnMsg.setOnClickListener(this);
        mBtnMe.setOnClickListener(this);
        mRecordTip = mRootView.findViewById(R.id.record_tip);
        mRecordTip.setOnClickListener(this);
        mAnimation = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, DpUtil.dp2px(5));
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setDuration(400);


        mSparseArray = new SparseArray<>();
        //首页
        mHomeFragment = new HomeFragment();
        mSparseArray.put(HOME, mHomeFragment);
        //关注
        mSparseArray.put(FOLLOW, new FollowFragment());
        //消息
        mSparseArray.put(MSG, new MessageFragment());
        //我
        UserFragment userFragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_MAIN_USER_CENTER, true);
        userFragment.setArguments(bundle);
        mSparseArray.put(ME, userFragment);
        mCurKey = HOME;
        mFragmentManager = getChildFragmentManager();
        FragmentTransaction tx = mFragmentManager.beginTransaction();
        for (int i = 0, size = mSparseArray.size(); i < size; i++) {
            Fragment fragment = mSparseArray.valueAt(i);
            tx.add(R.id.replaced, fragment);
            if (mSparseArray.keyAt(i) == mCurKey) {
                tx.show(fragment);
            } else {
                tx.hide(fragment);
            }
        }
        tx.commit();
        refreshUnReadCount();
    }

    @Override
    protected void reloadUi() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                toggleHome();
                break;
            case R.id.btn_follow:
                toggleFollow();
                break;
            case R.id.btn_msg:
                toggleMsg();
                break;
            case R.id.btn_me:
                toggleMe();
                break;
            case R.id.record_tip:
                ((MainActivity) mContext).checkVideoPermission();
                break;
        }
    }

    /**
     * 切换到Home
     */
    private void toggleHome() {
        toggle(HOME);
        if (mBtnHome != null) {
            mBtnHome.doToggle();
        }
        if (mHomeFragment.isRecommend()) {
            setCanScroll(true);
        }
        toggleRecordTip(false);
    }

    /**
     * 切换到关注
     */
    private void toggleFollow() {
        if (AppConfig.getInstance().isLogin()) {
            toggle(FOLLOW);
            if (mBtnFollow != null) {
                mBtnFollow.doToggle();
            }
            setCanScroll(false);
            toggleRecordTip(false);
        } else {
            forwardLogin();
        }

    }

    /**
     * 切换到消息
     */
    private void toggleMsg() {
        if (AppConfig.getInstance().isLogin()) {
            toggle(MSG);
            if (mBtnMsg != null) {
                mBtnMsg.doToggle();
            }
            setCanScroll(false);
            toggleRecordTip(false);
        } else {
            forwardLogin();
        }
    }

    /**
     * 切换到我的
     */
    private void toggleMe() {
        if (AppConfig.getInstance().isLogin()) {
            toggle(ME);
            if (mBtnMe != null) {
                mBtnMe.doToggle();


            }
            setCanScroll(false);
        } else {
            forwardLogin();
        }


    }


    private void toggle(int key) {
        if (key == mCurKey) {
            return;
        }
        mCurKey = key;
        FragmentTransaction tx = mFragmentManager.beginTransaction();
        for (int i = 0, size = mSparseArray.size(); i < size; i++) {
            Fragment fragment = mSparseArray.valueAt(i);
            if (mSparseArray.keyAt(i) == mCurKey) {
                tx.show(fragment);
            } else {
                tx.hide(fragment);
            }
        }
        tx.commitAllowingStateLoss();
    }

    private void forwardLogin() {
        LoginActivity.forwardLogin(mContext);
    }

    public void hiddenChanged(boolean hidden) {
        if (mHomeFragment != null) {
            mHomeFragment.hiddenChanged(hidden);
        }
    }

    private void setCanScroll(boolean canScroll) {
        ((MainActivity) mContext).setCanScroll(canScroll);
    }

    public void onLogout() {
        mLogout = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLogout) {
            toggleHome();
        }
        if (AppConfig.getInstance().isLogin()) {
            mLogout = false;
            toggleRecordTip(mShowingRecordTip);
        }
    }

    public void showRecordTip(boolean show) {
        if (mCurKey == ME) {
            toggleRecordTip(show);
        }
    }

    private void toggleRecordTip(boolean show) {
        if (mShowingRecordTip == show) {
            return;
        }
        mShowingRecordTip = show;
        if (mRecordTip != null && mAnimation != null) {
            if (show) {
                if (mRecordTip.getVisibility() != View.VISIBLE) {
                    mRecordTip.setVisibility(View.VISIBLE);
                }
                mRecordTip.startAnimation(mAnimation);
            } else {
                mRecordTip.clearAnimation();
                if (mRecordTip.getVisibility() == View.VISIBLE) {
                    mRecordTip.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void refreshUnReadCount() {
        if (mRedPoint == null) {
            return;
        }
        if (AppConfig.getInstance().isLogin() && AppConfig.getInstance().isLoginIM()) {
            String unRedCount = JMessageUtil.getInstance().getAllUnReadMsgCount();
            if (!TextUtils.isEmpty(unRedCount) && !"0".equals(unRedCount)) {
                if (mRedPoint.getVisibility() != View.VISIBLE) {
                    mRedPoint.setVisibility(View.VISIBLE);
                }
                mRedPoint.setText(unRedCount);
            } else {
                if (mRedPoint != null && mRedPoint.getVisibility() == View.VISIBLE) {
                    mRedPoint.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            if (mRedPoint.getVisibility() == View.VISIBLE) {
                mRedPoint.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mRecordTip != null) {
            mRecordTip.clearAnimation();
        }
        super.onDestroy();
    }

}
