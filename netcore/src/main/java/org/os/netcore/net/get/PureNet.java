package org.os.netcore.net.get;


import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.String.valueOf;

public class PureNet {
    private static OkHttpClient client = new OkHttpClient();

    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful())
            return response.body().string();
        return "";
    }

    public static byte[] getBytes(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful())
            return response.body().bytes();
        return null;
    }

    /**
     *  application/json
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String post(String url, String json) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"), json.getBytes("UTF-8")))
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful())
            return response.body().string();
        return "";
    }

    /**
     *  application/json
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static byte[] postReturnBytes(String url, String json) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"), json.getBytes("UTF-8")))
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful())
            return response.body().bytes();
        return null;
    }

    /**
     *
     * @param url
     * @param severReceivedName 服务端接收文件的名称 e.g 'file'
     * @param params
     * @param file
     * @param mediaType
     * @return
     * @throws IOException
     */
    public static Response upLoadFile(String url,String severReceivedName, Map<String, String> params, File file, MediaType mediaType) throws IOException {
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(mediaType, file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart(severReceivedName, file.getName(), body);
        }
        if (params != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : params.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).build();
        // readTimeout("请求超时时间" , 时间单位);
        Response response = client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).execute();
        return response;
    }

    /**
     *
     * @param url
     * @param params  参数
     *                <p>
     *                 Map《serverReceivedName,File》，
     *                 Map《key,param>》
     *                </p>
     * @return
     * @throws IOException
     */
    public static Response uploadFile(String url, Map<String, Object> params) throws IOException{
        OkHttpClient okHttpClient1 = new OkHttpClient();
        MultipartBody.Builder builder1 = new MultipartBody.Builder();
        builder1.setType(MultipartBody.FORM);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof File) {//如果请求的值是文件
                File file = (File) value;
                //MediaType.parse("application/octet-stream")以二进制的形式上传文件
                builder1.addFormDataPart(key, ((File) value).getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            } else {
                //如果请求的值是string类型
                builder1.addFormDataPart(key, value.toString());
            }
        }
        Request request1 = new Request.Builder().post(builder1.build()).url(url).build();
        return okHttpClient1.newCall(request1).execute();
    }


}
