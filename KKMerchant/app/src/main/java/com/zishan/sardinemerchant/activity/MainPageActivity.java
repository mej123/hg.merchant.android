package com.zishan.sardinemerchant.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.fragment.page.MainPageFragment;
import com.zishan.sardinemerchant.fragment.personal.MainPersonFragment;
import com.zishan.sardinemerchant.view.BottomTabItem;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.ActivityManager;

/**
 * 首页
 * Created by wislie on 2017/9/12.
 */

public class MainPageActivity extends BaseFragmentActivity {


    @BindView(R.id.tab_item)
    BottomTabItem mTabItem;

    private int[] selectedImages = {R.mipmap.selected_mainpage, R.mipmap.selected_personal};
    private int[] unselectedImages = {R.mipmap.unselected_mainpage, R.mipmap.unselected_personal};
    private int[] titles = new int[]{R.string.main_page, R.string.main_personal};

    private MainPageFragment mPageFragment;

    private MainPersonFragment mPersonalFragment;
    private BFragment mCurrentFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mainpage;
    }


    @Override
    protected void initContentView() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mPageFragment == null) {
            mPageFragment = new MainPageFragment();
        }
        mCurrentFragment = mPageFragment;
        ft.replace(R.id.fragmentContainer, mCurrentFragment).commit();

        mTabItem.setTabAttrs(selectedImages, unselectedImages, titles);
        mTabItem.setTabSelectedListener(new BottomTabItem.TabSelectedListener() {
            @Override
            public void select(int position) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (position){
                    case 0:
                        if (mPageFragment == null) {
                            mPageFragment = new MainPageFragment();
                        }
                        switchContent(mCurrentFragment, mPageFragment);
                        break;

                    case 1:
                        if (mPersonalFragment == null) {
                            mPersonalFragment = new MainPersonFragment();
                        }
                        switchContent(mCurrentFragment, mPersonalFragment);
                        break;
                }
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    /**
     * 当fragment进行切换时，采用隐藏与显示的方法加载fragment以防止数据的重复加载
     *
     * @param from
     * @param to
     */
    public void switchContent(BFragment from, BFragment to) {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                // 隐藏当前的fragment，add下一个到Activity中
                ft.hide(from).add(R.id.fragmentContainer, to).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                ft.hide(from).show(to).commit();
            }
        }
    }

    //手机连续按返回键两次的间隔时间
    private long mExitTime;

    //按系统返回键时出发
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                ToastUtil.cancel();
                ActivityManager.getInstance().finishAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
