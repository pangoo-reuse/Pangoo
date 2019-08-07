//  This codes are generated automatically. Do not modifyActivity!
package com.os.pangoo.activity;

import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.os.lib.help.ExcludeView;
import com.os.pangoo.BaseActivity;
import com.os.pangoo.R;
import com.os.pangoo.databinding.AbcBinding;
import com.os.pangoo.vm.AbcViewModel;
import java.lang.Override;
import java.lang.System;

/**
 * this is autoExclude class ,please dont delete inject 'ExcludeView';
 */
@ExcludeView
public class Abc extends BaseActivity {
  public AbcBinding abcBinding;

  @Override
  protected View ui(Bundle sa) {
    abcBinding = DataBindingUtil.setContentView(this, R.layout.abc);
    AbcViewModel abcViewModel = new AbcViewModel();
    abcBinding.setViewModel(abcViewModel);
    return null;
  }

  @Override
  protected void statusBar(Bundle sa) {
    System.out.println("hello , Pangoo !");
  }
}
