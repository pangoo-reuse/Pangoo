//  This codes are generated automatically. Do not modifyActivity!
package com.os.pangoo.activity;

import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.os.lib.help.ExcludeView;
import com.os.pangoo.BaseActivity;
import com.os.pangoo.R;
import com.os.pangoo.databinding.ActivityLoginBinding;
import com.os.pangoo.vm.ActivityLoginViewModel;
import java.lang.Override;
import java.lang.System;

/**
 * this is autoExclude class ,please dont delete inject 'ExcludeView';
 */
@ExcludeView
public class ActivityLogin extends BaseActivity {
  public ActivityLoginBinding activityLoginBinding;

  @Override
  protected View ui(Bundle sa) {
    activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    ActivityLoginViewModel activityLoginViewModel = new ActivityLoginViewModel();
    activityLoginBinding.setViewModel(activityLoginViewModel);
    return null;
  }

  @Override
  protected void statusBar(Bundle sa) {
    System.out.println("hello , Pangoo !");
  }
}
