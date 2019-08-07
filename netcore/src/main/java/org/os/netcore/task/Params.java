package org.os.netcore.task;


import android.text.TextUtils;

import org.os.netcore.NetConfigBuilder;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Params {

    private Params() {
    }

    private static Params params;

    public static Params getInstance() {
        if (params == null) params = new Params();
        return params;
    }

    public TreeMap<String, Object> doParams(NameValuePair... nameValues) {
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        if (nameValues != null && nameValues.length != 0) {
            for (NameValuePair nms :
                    nameValues) {
                params.put(nms.getName(), nms.getValue());
            }
        }
        addCommonParams(params);
        return params;
    }

    public TreeMap<String, Object> addToParams(TreeMap<String, Object> params, NameValuePair... nameValues) {
        if (nameValues != null && nameValues.length != 0) {
            for (NameValuePair nms :
                    nameValues) {
                params.put(nms.getName(), nms.getValue());
            }
        }
        return params;
    }

    public TreeMap<String, Object> doParams() {
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        addCommonParams(params);
        return params;
    }

    public TreeMap<String, Object> doParams(List<NameValuePair> nameValues) {
        TreeMap<String, Object> params = new TreeMap<>();
        if (nameValues != null && nameValues.size() != 0) {
            for (NameValuePair nms :
                    nameValues) {
                params.put(nms.getName(), nms.getValue());
            }
        }
        addCommonParams(params);
        return params;
    }

    public TreeMap<String, Object> doParams(TreeMap<String, Object> params) {
        addCommonParams(params);
        return params;
    }

    private void addCommonParams(TreeMap<String, Object> params) {
        if (params != null) {
            Map<String, Object> commonParams = NetConfigBuilder.getInstance().getCommonParams();
            if (commonParams != null && !commonParams.isEmpty()) {
                params.putAll(commonParams);
            }

        }

    }

    public RequestBody buildRequest(TreeMap<String, String> params, String fileName, String fileField/*与服务端约定的文件上传的参数名*/, String... filePaths) {
        File[] files = null;
        if (filePaths != null && filePaths.length != 0) {
            files = new File[filePaths.length];
            for (int i = 0; i < filePaths.length; i++) {
                files[i] = new File(filePaths[i]);
            }

        }
        return buildRequest(params, fileName, fileField, files);
    }

    public RequestBody buildRequest(TreeMap<String, String> params, String fileName, String fileField/*与服务端约定的文件上传的参数名*/, File... files) {
        if (files == null || files.length == 0) return null;
        //构建body
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry :
                params.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }

        if (files != null && files.length != 0) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String mimeType = getMimeType(file.getAbsolutePath());
//                filesBody.put("mpx" + "\"; filename=\"" + file.getName(), RequestBody.createOrUpdate(MediaType.parse(mimeType), file));
                builder.addFormDataPart(fileField, TextUtils.isEmpty(fileName) ? file.getName() : fileName, RequestBody.create(MediaType.parse(mimeType), file));
            }
        }
        RequestBody requestBody = builder.build();
        return requestBody;
    }

    public String getMimeType(String fileUrl) {
        try {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            String type = fileNameMap.getContentTypeFor(fileUrl);

            return type;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param list<T> extends String
     * @return ["",""]
     */
    public String convertToArray(List list) {
        StringBuilder json = new StringBuilder("");
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String value = (String) list.get(i);
                if (i == 0 || i == list.size() - 1) {
                    json.append(" " + value);

                } else {
                    json.append(value);
                }

            }
        }
        return json.toString();
    }


    public RequestBody[] doImageBody(List<String> paths) {
        RequestBody[] bodies = new RequestBody[0];
        if (paths != null && paths.size() != 0) {
            bodies = new RequestBody[paths.size()];
            for (int i = 0; i < bodies.length; i++) {
                String path = paths.get(i);
                File file = new File(path);
                RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                bodies[i] = body;
            }
        }
        return bodies;
    }

    public TreeMap<String, RequestBody> doStringBody(NameValuePair<String>... nameValues) {
        TreeMap<String, RequestBody> params = new TreeMap<>();
        if (nameValues != null && nameValues.length != 0) {
            for (NameValuePair<String> fnv : nameValues) {
                String name = fnv.getName();
                String value = fnv.getValue();
                RequestBody body = RequestBody.create(null, value);
                params.put(name, body);
            }

        }
        return params;
    }

    public String buildGetParams(NameValuePair... params) {
        String uri = "";
        if (params != null && params.length != 0) {
            for (NameValuePair param : params) {
                String name = param.getName();
                Object value = param.getValue();
                uri += name + "=" + value + "&";

            }
            uri = uri.substring(0, uri.lastIndexOf("&"));
        }
        return uri;
    }

    public String buildGetUri(String host, Map<String, String> params) {
        if (!host.endsWith("?")) host += "?";
        String uri = "";
        if (params != null && params.size() != 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue() == null ? "null" : entry.getValue();
                uri += name + "=" + value + "&";

            }
            uri = uri.substring(0, uri.lastIndexOf("&"));
        }
//        return URLEncoder.encode(host + uri);
        return host + uri;
    }

    public String buildGetParams(TreeMap<Object, Object> params) {
        String uri = "";
        if (params != null && params.size() != 0) {
            for (Map.Entry<Object, Object> entry : params.entrySet()) {
                String name = (String) entry.getKey();
                Object value = entry.getValue();
                uri += name + "=" + value + "&";

            }
            uri = uri.substring(0, uri.lastIndexOf("&"));
        }
        return uri;
    }
}
