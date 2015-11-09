package com.dinghu.ui.helper;

import android.app.Activity;

import com.dinghu.R;

import cn.common.ui.BaseDialog;

/**
 * 描述:用于处理显示加载进度的dialog
 *
 * @author jakechen
 * @since 2015/11/9 16:28
 */
public class LoadingDialogHelper {
  private Activity mActivity;
  private BaseDialog mLoadingDialog;

  public LoadingDialogHelper(Activity activity) {
    mActivity = activity;
  }

  public void show() {
    if (mActivity != null && !mActivity.isFinishing()) {
      if (mLoadingDialog == null) {
        mLoadingDialog = new BaseDialog(mActivity);
        mLoadingDialog.setWindow(R.style.alpha_animation, 0.0f);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
      }
      mLoadingDialog.show();
    }
  }

  public void hide() {
    if (mLoadingDialog != null) {
      mLoadingDialog.dismiss();
    }
  }
}
