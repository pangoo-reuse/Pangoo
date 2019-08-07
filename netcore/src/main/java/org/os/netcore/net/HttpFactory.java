package org.os.netcore.net;


import org.os.netcore.NetConfigBuilder;
import org.os.netcore.init.NetRequestListener;
import org.os.netcore.net.exception.ErrorProcessor;
import org.os.netcore.net.exception.GlobalException;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class HttpFactory {
    private ErrorProcessor errorProcessor;

    public <D> Subscription request(Observable<? extends D> requestObs, final NetRequestListener<D> requestListener) {
        return requestObs.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .filter(new Func1<D, Boolean>() {
                    @Override
                    public Boolean call(D m) {
                        return m != null;
                    }
                })
                .subscribe(new Subscriber<D>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        if (requestListener != null) requestListener.onRequestStart();

                    }

                    @Override
                    public void onCompleted() {
                        if (requestListener != null) requestListener.onRequestCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (requestListener != null) {
                            errorProcessor = NetConfigBuilder.getInstance().getErrorProcessor();
                            if (errorProcessor == null) {
                                requestListener.onRequestError(new GlobalException(e));
                            } else requestListener.onRequestError(errorProcessor.processor(e));

                        }
                    }

                    @Override
                    public void onNext(D data) {
                        if (requestListener != null) requestListener.onRequestSucceeded(data);
                    }
                });
    }
}
