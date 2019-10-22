package org.os.netcore;

import org.os.netcore.init.Hook;
import org.os.netcore.init.JsonConvert;
import org.os.netcore.init.SignInterface;
import org.os.netcore.net.exception.ErrorProcessor;
import org.os.netcore.net.exception.GlobalException;
import org.os.netcore.task.NameValuePair;

import okhttp3.OkHttpClient;

public class NetConfigBuilder {
    private OkHttpClient okHttpClient;
    private SignInterface signInterface;
    private JsonConvert jsonConvert;
    private ErrorProcessor errorProcessor;
    private NameValuePair<?>[] commonParams;
    private Hook hook;

    private static volatile NetConfigBuilder ourInstance;

    public static NetConfigBuilder getInstance() {
        if (ourInstance == null) {
            synchronized (NetConfigBuilder.class) {
                if (ourInstance == null) ourInstance = new NetConfigBuilder();
            }
        }
        return ourInstance;
    }

    private NetConfigBuilder() {
    }

    public <D> Hook<D> getHook() {
        return hook;
    }

    public <D> NetConfigBuilder setHook(Hook<D> hook) {
        this.hook = hook;
        return this;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public SignInterface getSignInterface() {
        return signInterface;
    }

    public NetConfigBuilder setSignInterface(SignInterface signInterface) {
        this.signInterface = signInterface;
        return this;
    }

    public NetConfigBuilder setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        return this;
    }

    public NetConfigBuilder setJsonConvert(JsonConvert jsonConvert) {
        this.jsonConvert = jsonConvert;
        return this;
    }

    public NetConfigBuilder setErrorProcessor(ErrorProcessor errorProcessor) {
        this.errorProcessor = errorProcessor;
        return this;
    }

    public JsonConvert getJsonConvert() {
        return jsonConvert;
    }

    public ErrorProcessor getErrorProcessor() {
        return errorProcessor != null ? errorProcessor : new ErrorProcessor() {
            @Override
            public GlobalException processor(Throwable throwable) {
                return new GlobalException(throwable);
            }
        };
    }


    public NameValuePair<?>[] getCommonParams() {
        return commonParams;
    }

    public NetConfigBuilder setCommonParams(NameValuePair<?>... params) {

        this.commonParams = params;
        return this;
    }


}
