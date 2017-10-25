package com.android.weatherforecast.webservices.common;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitHelper {

    public static  String BASE_URL = "http://api.openweathermap.org/data/2.5/";


    private static Retrofit.Builder retroBuilder = null;



    public static Retrofit getInstance() {

        retroBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHeaderIntercepter());
        return retroBuilder.build();
    }





    public  static SSLSocketFactory getDefaultCLient(){
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
            okHttpClient.sslSocketFactory(sslSocketFactory);
            okHttpClient.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return sslSocketFactory;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public  static OkHttpClient getHeaderIntercepter() {

           OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.sslSocketFactory(getDefaultCLient());
        okHttpClient.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
            okHttpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder reqBuilder = original.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json;versions=1")
                            .method(original.method(), original.body());
                    Request request = reqBuilder.build();
                    return chain.proceed(request);
                }
            });
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    System.out.println(message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            okHttpClient.addInterceptor(loggingInterceptor);
        OkHttpClient httpClient = okHttpClient.readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS).build();

        return httpClient;
    }


}
