//package com.os.pangoo.task;
//
//import org.os.netcore.task.NameValuePair;
//import org.os.netcore.task.RootBaseTask;
//
//import java.util.TreeMap;
//
//import rx.Observable;
//
//public class AppBaseTask extends RootBaseTask {
//
//    public AppBaseTask(NameValuePair<?>... params) {
//        super("https://mobsec-dianhua.baidu.com", params);
//
//    }
//
//    @Override
//    protected Observable<?> build(TreeMap<String, Object> params) {
//        Observable<String> test = api.test("/dianhua_api/open/location", params);
//        return test;
//    }
//
//
//}
