package com.erdogan.istanbulispark.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiRequest {
    //static String ip = ipv4Helper.getInstance().getLocalAddress();
    public static String BASEURL = "https://api.ibb.gov.tr/";

    // Get Retrofit instance
    private static Retrofit getRefrofitInstance() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .build();
        return rf;
    }


    private static Retrofit getRefrofitInstanceOnly() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .build();
        return rf;
    }


    // Return Api Service
    public static APIInterface getApiService() {
        APIInterface api = getRefrofitInstance().create(APIInterface.class);
        return api;
    }

    public static APIInterface getApiServiceOnly() {
        APIInterface api = getRefrofitInstanceOnly().create(APIInterface.class);
        return api;
    }
}
