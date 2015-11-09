
package com.dinghu.ui.fragment;

import com.dinghu.R;
import com.dinghu.data.BroadcastActions;
import com.dinghu.data.InitShareData;
import com.dinghu.ui.activity.HistoryWorkListActivity;
import com.dinghu.ui.activity.LoginActivity;
import com.dinghu.ui.activity.ModifyPwActivity;
import com.dinghu.ui.activity.MyAccountBookActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.common.ui.fragment.BaseWorkerFragment;
import cn.common.ui.widgt.ChangeThemeUtils;

/**
 * 用户中心页面
 */
public class UserCenterFragment extends BaseWorkerFragment implements View.OnClickListener {

    public static UserCenterFragment newInstance() {
        return new UserCenterFragment();
    }

    private TextView mTvTitle;

    private Button mBtnExit;

    private View mVModifyPw;

    private View mVHistoryWorkList;

    private View mVMyAccountBook;

    @Override
    public void initView() {
        setContentView(R.layout.fragment_user_center);
        mBtnExit = (Button) findViewById(R.id.btn_exit);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        ChangeThemeUtils.adjustStatusBar(findViewById(R.id.rl_title), getActivity());
        mVModifyPw = findViewById(R.id.ll_modify_pw);
        mVHistoryWorkList = findViewById(R.id.ll_history_work_list);
        mVMyAccountBook = findViewById(R.id.ll_my_account_book);
    }

    @Override
    protected void initEvent() {
        mVModifyPw.setOnClickListener(this);
        mVHistoryWorkList.setOnClickListener(this);
        mVMyAccountBook.setOnClickListener(this);
        mBtnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_modify_pw) {
            goActivity(ModifyPwActivity.class);
        } else if (id == R.id.ll_history_work_list) {
            goActivity(HistoryWorkListActivity.class);
        } else if (id == R.id.ll_my_account_book) {
            goActivity(MyAccountBookActivity.class);
        } else if (id == R.id.btn_exit) {
            InitShareData.setUserId(-1);
            sendBroadcast(BroadcastActions.ACTION_EXIT_TO_LOGIN);
            goActivity(LoginActivity.class);
        }
    }
}
