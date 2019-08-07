package org.os.netcore.init;

import java.lang.reflect.Type;

public interface JsonConvert<D> {
    <D> D convert(String result, Type type);
}
