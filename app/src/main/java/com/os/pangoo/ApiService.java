package com.os.pangoo;

import com.os.netcoreannatation.RestApi;

import java.util.TreeMap;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

@RestApi
public interface ApiService {
    @GET
    Observable<String> test(@Url String uri, @QueryMap TreeMap<String, Object> params);
}
