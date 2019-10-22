package com.os.pangoo;

import android.content.Context;
import android.widget.TextView;

import com.os.lib.RootViewModel;

@RootViewModel()
public abstract class BaseViewModel {
    void a() {
    }

    public BaseViewModel(TextView tv, Context context) {
    }

    protected abstract void m(int a, Double b);

    protected abstract BaseActivity mxc(String a, Double b);


}
