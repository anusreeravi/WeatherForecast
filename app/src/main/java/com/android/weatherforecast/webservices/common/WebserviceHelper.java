package com.android.weatherforecast.webservices.common;

//import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WebserviceHelper {


    public WebserviceHelper() {

    }

    protected <T> T createEndpoint(Class<T> endpoint) {
        return RetrofitHelper.getInstance().create(endpoint);
    }


    protected <T> void executeWebservice(final Call<T> retroCall, final WebserviceCallBack callBack) {
        callBack.showProgress();
        retroCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());

                }
                else {
                    callBack.onFailed(response.message());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callBack.onError(t.getMessage());
            }
        });
    }


}
