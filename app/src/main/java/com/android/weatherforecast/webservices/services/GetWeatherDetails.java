package com.android.weatherforecast.webservices.services;


import com.android.weatherforecast.models.WeatherData;
import com.android.weatherforecast.webservices.common.WebserviceCallBack;
import com.android.weatherforecast.webservices.common.WebserviceHelper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class GetWeatherDetails extends WebserviceHelper {

    public static final String API_KEY = "e16a2eee34472b6cd9bd73ec34ec4f4d";
    public void getDataByCityname(String city,WebserviceCallBack callBack) {
        if(city.split(",")[1]==null || city.split(",")[1]=="")
        city = city + ","+"us";
        Call<WeatherData> payloadCall = createEndpoint(DataNameEndpoints.class).getDataByName(city,API_KEY);
        executeWebservice(payloadCall, callBack);
    }
    public void getDataByCoordinates(String lat,String lon,WebserviceCallBack callBack) {
        Call<WeatherData> payloadCall = createEndpoint(DataByCoordinatesEndpoints.class).getDataByCoordinates(lat,lon,API_KEY);
       executeWebservice(payloadCall, callBack);
        }
    public void getDataByZip(String zip,WebserviceCallBack callBack) {
       Call<WeatherData> payloadCall = createEndpoint(DataByZipEndpoints.class).getDataByZip(zip,API_KEY);
        executeWebservice(payloadCall, callBack);
    }
    public void getDataByCityID(String cityId,WebserviceCallBack callBack) {
       Call<WeatherData> payloadCall = createEndpoint(DataByIdEndpoints.class).getDataById(cityId,API_KEY);
       executeWebservice(payloadCall, callBack);
    }


    public interface DataNameEndpoints {
        @GET("forecast")
        Call<WeatherData> getDataByName(@Query("q") String city,@Query("appid") String appid);

    }

    public interface DataByCoordinatesEndpoints {
        @GET("forecast")
              Call<WeatherData> getDataByCoordinates(@Query("lat") String id,@Query("lon") String lon,@Query("appid") String appid);

    }
    public interface DataByIdEndpoints {
        @GET("forecast")
        Call<WeatherData> getDataById(@Query("id") String id,@Query("appid") String appid);

    }
    public interface DataByZipEndpoints {
        @GET("forecast")
        Call<WeatherData> getDataByZip(@Query("zip") String zip,@Query("appid") String appid);

    }


}
