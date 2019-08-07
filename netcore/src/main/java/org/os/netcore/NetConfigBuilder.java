package org.os.netcore;

import org.os.netcore.init.JsonConvert;
import org.os.netcore.init.Sign;
import org.os.netcore.net.exception.ErrorProcessor;
import org.os.netcore.net.exception.GlobalException;

import java.util.Map;

import okhttp3.OkHttpClient;

public class NetConfigBuilder {
    private OkHttpClient okHttpClient;
    private Sign sign;
    private JsonConvert jsonConvert;
    private ErrorProcessor errorProcessor;
    private Map<String, Object> commonParams;

    private static NetConfigBuilder ourInstance;

    public static NetConfigBuilder getInstance() {
        if (ourInstance == null) {
            synchronized (NetConfigBuilder.class) {
                if (ourInstance == null) ourInstance = new NetConfigBuilder();
            }
        }
        return ourInstance;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public Sign getSignFactory() {
        return sign;
    }

    private NetConfigBuilder() {
    }

    public NetConfigBuilder setSignFactory(Sign sign) {
        this.sign = sign;
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
        return errorProcessor != null ? errorProcessor: new ErrorProcessor() {
            @Override
            public GlobalException processor(Throwable throwable) {
                return new GlobalException(throwable);
            }
        };
    }


    public Map<String, Object> getCommonParams() {
        return commonParams;
    }

    public NetConfigBuilder setCommonParams(Map<String, Object> commonParams) {
        this.commonParams = commonParams;

        return this;
    }


}
