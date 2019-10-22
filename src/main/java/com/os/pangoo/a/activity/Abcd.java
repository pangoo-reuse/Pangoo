//  This codes are generated automatically. Do not modifyActivity!
package com.os.pangoo.a.activity;

import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.os.lib.help.ExcludeView;
import com.os.pangoo.BaseActivity;
import com.os.pangoo.R;
import com.os.pangoo.databinding.AbcdBinding;
import com.os.pangoo.vm.AbcdViewModel;
import java.lang.Override;
import java.lang.System;

/**
 * this is autoExclude class ,please dont delete inject 'ExcludeView';
 */
@ExcludeView
public class Abcd extends BaseActivity {
  public AbcdBinding abcdBinding;

  @Override
  protected View ui(Bundle sa) {
    abcdBinding = DataBindingUtil.setContentView(this, R.layout.abcd);
    AbcdViewModel abcdViewModel = new AbcdViewModel();
    abcdBinding.setViewModel(abcdViewModel);
    return null;
  }

  @Override
  protected void statusBar(Bundle sa) {
    System.out.println("hello , Pangoo !");
  }
}
