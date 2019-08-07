package com.os.pangoo;

import android.app.Application;

import org.os.netcore.NetConfigBuilder;
import org.os.netcore.net.exception.ErrorProcessor;
import org.os.netcore.net.exception.GlobalException;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetConfigBuilder.getInstance().setErrorProcessor(new ErrorProcessor() {
            @Override
            public GlobalException processor(Throwable throwable) {
                GlobalException exception = new GlobalException(new Throwable("test"));
                return exception;
            }
        });
    }
}
