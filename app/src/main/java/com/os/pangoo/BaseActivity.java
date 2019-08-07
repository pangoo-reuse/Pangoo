package com.os.pangoo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.os.lib.RootActivity;
import com.os.lib.SuperLayoutMethod;

@RootActivity
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuperLayoutMethod
    protected abstract View ui(Bundle sa);

    protected abstract void statusBar(Bundle sa);
}
