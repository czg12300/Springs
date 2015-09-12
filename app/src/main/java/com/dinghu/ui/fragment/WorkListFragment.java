
package com.dinghu.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.dinghu.R;

import java.util.HashMap;
import java.util.List;

import cn.common.ui.fragment.BaseWorkerFragment;
import cn.common.ui.widgt.ChangeThemeUtils;

/**
 * 描述：工单页面
 *
 * @author jake
 * @since 2015/9/12 11:36
 */

public class WorkListFragment extends BaseWorkerFragment {


    public static WorkListFragment newInstance() {
        return new WorkListFragment();
    }

    private RadioGroup mRgMode;
    /**
     * 当前的fragment的id
     */
    private int currentTab = R.id.rb_mode_list;
    /**
     * 存放tab的fragment
     */
    private HashMap<Integer, Fragment> mTabFragments;
    private FrameLayout fl;

    @Override
    protected void initView() {
        setContentView(R.layout.fragment_work_list);
        ChangeThemeUtils.adjustStatusBar(findViewById(R.id.fl_title), getActivity());
        mRgMode = (RadioGroup) findViewById(R.id.rg_mode);
        fl = (FrameLayout) findViewById(R.id.fl_fragment_content);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        findViewById(R.id.iv_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRgMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                showCurrentFragment(checkedId);
                currentTab = checkedId;

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mTabFragments = new HashMap<Integer, Fragment>();
        mTabFragments.put(R.id.rb_mode_list, ModeListFragment.newInstance());
        mTabFragments.put(R.id.rb_mode_map, ModeMapFragment.newInstance());
        getFragmentManager().beginTransaction().add(R.id.fl_fragment_content, mTabFragments.get(R.id.rb_mode_list)).commit();
    }

    /**
     * 显示当前的fragment
     *
     * @param checkedId
     */
    private void showCurrentFragment(int checkedId) {
        Fragment toFragment = mTabFragments.get(checkedId);
        Fragment fromFragment = mTabFragments.get(currentTab);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        fromFragment.onPause(); // 暂停当前tab
        if (toFragment.isAdded()) {
            toFragment.onResume(); // 启动目标tab的onResume()
        } else {
            ft.add(R.id.fl_fragment_content, toFragment);
        }
        ft.hide(fromFragment);
        ft.show(toFragment);
        ft.commitAllowingStateLoss();
    }

}
