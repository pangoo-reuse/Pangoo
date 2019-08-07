package org.os.netcore.net;

import java.io.Serializable;

/**
 * Created by yone on 2017/9/13.
 */

public class NetBase<D> implements Serializable {
    protected String message;
    protected String sign;
    protected int code;
    protected D data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
