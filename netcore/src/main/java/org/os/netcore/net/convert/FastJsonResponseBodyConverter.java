package org.os.netcore.net.convert;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import org.os.netcore.NetConfigBuilder;
import org.os.netcore.init.JsonConvert;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class FastJsonResponseBodyConverter<D> implements Converter<ResponseBody, D> {


    private Type mType;

    private ParserConfig config;
    private int featureValues;
    private Feature[] features;

    FastJsonResponseBodyConverter(Type type, ParserConfig config, int featureValues,
                                  Feature... features) {
        mType = type;
        this.config = config;
        this.featureValues = featureValues;
        this.features = features;
    }

    @Override
    public D convert(ResponseBody value) throws IOException {
        try {
            try {
                String data = value.string();
                JsonConvert<D> jsonConvert = NetConfigBuilder.getInstance().getJsonConvert();
                if (jsonConvert == null) {
                    return InternalJsonConvert.parseJsonObject(data, mType, config, featureValues, features);
                }
                return jsonConvert.convert(data, mType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } finally {
            value.close();
        }
    }


}
