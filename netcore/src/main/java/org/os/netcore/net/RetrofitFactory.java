package org.os.netcore.net;


import android.text.TextUtils;

import org.os.netcore.NetConfigBuilder;
import org.os.netcore.net.convert.FastJsonConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class RetrofitFactory {


    private static volatile RetrofitFactory retroFactory;

    public static RetrofitFactory getInstants() {
        if (retroFactory == null) {
            synchronized (RetrofitFactory.class) {
                if (retroFactory == null) {
                    retroFactory = new RetrofitFactory();
                }
            }
        }
        return retroFactory;
    }

    /**
     * @param domain e.g https://xxx.com
     *               <p>not end with / </p>
     * @return
     */
    public Retrofit createJsonRetrofit(String domain) {
        OkHttpClient client = NetConfigBuilder.getInstance().getOkHttpClient();
        Retrofit.Builder builder = new Retrofit.Builder()
                .client(client == null ? new OkHttpClient() : client)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        if (!TextUtils.isEmpty(domain)) builder.baseUrl(domain);
        return builder.build();
    }


}