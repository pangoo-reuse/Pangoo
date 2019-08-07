package org.os.netcore.net.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

final class FastJsonRequestBodyConverter<D> implements Converter<D, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private final Retrofit retrofit;
    private final Annotation[] parameterAnnotations;
    private final Annotation[] methodAnnotations;
    private SerializeConfig serializeConfig;
    private SerializerFeature[] serializerFeatures;

    FastJsonRequestBodyConverter(Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit, SerializeConfig config, SerializerFeature... features) {
        serializeConfig = config;
        serializerFeatures = features;
        this.retrofit = retrofit;
        this.parameterAnnotations = parameterAnnotations;
        this.methodAnnotations = methodAnnotations;
    }

    @Override
    public RequestBody convert(D value) throws IOException {
        byte[] content;
        if (serializeConfig != null) {
            if (serializerFeatures != null) {
                content = JSON.toJSONBytes(value, serializeConfig, serializerFeatures);
            } else {
                content = JSON.toJSONBytes(value, serializeConfig);
            }
        } else {
            if (serializerFeatures != null) {
                content = JSON.toJSONBytes(value, serializerFeatures);
            } else {
                content = JSON.toJSONBytes(value);
            }
        }
        return RequestBody.create(MEDIA_TYPE, content);
    }
}
