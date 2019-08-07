package org.os.netcore.task;

import org.os.netcore.init.NetRequestListener;
import org.os.netcore.net.HttpFactory;

import java.util.TreeMap;

import rx.Observable;
import rx.Subscription;


public abstract class Task {
    private HttpFactory httpFactory;
    private TreeMap<String, Object> params;


    public Task(NameValuePair<?>... params) {
        this.params = Params.getInstance().doParams(params);
        if (httpFactory == null)
            httpFactory = new HttpFactory();
    }


    protected abstract Observable<?> build(TreeMap<String, Object> params);

    public <D> Subscription run(NetRequestListener<D> requestListener) {
        Observable<?> observable = build(params);
        return httpFactory.request((Observable<? extends D>) observable, requestListener);
    }

}