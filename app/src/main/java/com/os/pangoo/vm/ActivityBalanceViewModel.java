// this codes are generated automatically. Do not modifyActivity!
package com.os.pangoo.vm;

import android.content.Context;
import android.widget.TextView;
import com.os.lib.help.ExcludeViewModel;
import com.os.pangoo.BaseActivity;
import com.os.pangoo.BaseViewModel;
import java.lang.Double;
import java.lang.Override;
import java.lang.String;
import java.lang.System;

/**
 * this is autoExclude class ,please dont delete inject 'ExcludeViewModel';
 */
@ExcludeViewModel
public final class ActivityBalanceViewModel extends BaseViewModel {
  public ActivityBalanceViewModel(TextView tv, Context context) {
    super(tv,context);
  }

  @Override
  protected void m(int a, Double b) {
    System.out.println("hello , Pangoo !");
  }

  @Override
  protected BaseActivity mxc(String a, Double b) {
    System.out.println("hello , Pangoo !");
    return null;
  }
}
