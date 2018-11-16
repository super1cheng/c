package com.example.asus.tastenews.network;

import com.example.asus.tastenews.beans.WeatherBeanPackage.WeatherBean;
import com.example.asus.tastenews.utils.LogUtils;

import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 目前已经将网络上的数据通过Retrofit+RxJava取下来了，wonderful！
 * 应用于天气请求中
 */


public class NetworkResolver {
    public static void getResponse(String url, Map<String,String> params, final Callback callback){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
        LoadService service = retrofit.create(LoadService.class);
        service.loadNews(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherBean>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.d("RETR","onCompleted is done");
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                        LogUtils.d("RETR","e is " + e.getMessage());
                    }

                    @Override
                    public void onNext(WeatherBean newsBean) {
                        callback.onSuccess(newsBean);
                    }
                });
    }

    public static abstract class Callback{
        public abstract void onSuccess(Object object);
        public abstract void onFailure(Throwable e);
    }
}
