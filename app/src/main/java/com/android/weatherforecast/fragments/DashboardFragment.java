package com.android.weatherforecast.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.weatherforecast.R;
import com.android.weatherforecast.models.Forecast;
import com.android.weatherforecast.models.WeatherData;
import com.android.weatherforecast.models.WeatherDescription;
import com.android.weatherforecast.services.LocationUpdateService;
import com.android.weatherforecast.webservices.common.WebserviceCallBack;
import com.android.weatherforecast.webservices.services.GetWeatherDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DashboardFragment extends Fragment {

    private static final double Kelvin_to_degree=-273.15;
    private static final String DEGREE ="&#xb0;";
    View rootView;
    private static final String EMPTY_MESSAGE = "Fields are Empty.Please fill details";
    private static final String BY_CITY_NAME = "CITY NAME ,COUNTRY CODE";
    private static final String BY_CITY_ID = "CITY ID";
    private static final String BY_LAT_LONG = "GEOGRAPHIC CO_ORDINATES";
    private static final String BY_ZIP_CODE = "ZIP CODE,COUNTRY CODE";
    private RecyclerView weatherView;
    WeatherAdapter weatherAdapter;
    LocationManager locationManager;
    LocationListener locationListener;
    private double currentLat;
    private double currentLon;

    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        inflateViews(rootView);

        return rootView;
    }

    public void displayDialog(String message, Context context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void inflateViews(final View rootView) {

        //   String locationType = getArguments().getString("locationType");
        //   String location = getArguments().getString("location");
        final LocationUpdateService locationUpdateService = new LocationUpdateService(getActivity());
        locationUpdateService.getLocation(new LocationUpdateService.LocationStatusListener() {
            @Override
            public void onLocationRequested() {
                mProgressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.request_location));
            }

            @Override
            public void onLocationUpdated(double latitude, double longitude, float accuracy) {

                updateWeatherInfo(String.valueOf(latitude), String.valueOf(longitude));
                locationUpdateService.stopUsingGPS();
            }
        });
    }

    private void updateWeatherInfo(String stringLatitude, String stringLongitude) {
        String location = stringLatitude + "," + stringLongitude;
        String locationType = BY_LAT_LONG;
        if (location != null) {
            switch (locationType) {
                case BY_CITY_ID: {
                    new GetWeatherDetails().getDataByCityID(location, new WebserviceCallBack() {
                        ProgressDialog pd;

                        @Override
                        public void showProgress() {
                            pd = new ProgressDialog(getActivity());
                            pd.setMessage("Loading...");
                            pd.show();
                        }

                        @Override
                        public void onSuccess(Object response) {
                            if (pd.isShowing()) pd.cancel();

                            WeatherData message = (WeatherData) response;
                            populateData(message, rootView);
                        }

                        @Override
                        public void onError(String errorCode) {
                            if (pd.isShowing())
                                pd.cancel();
                        }

                        @Override
                        public void onFailed(String response) {
                            if (pd.isShowing())
                                pd.cancel();
                        }

                    });
                    break;
                }
                case BY_CITY_NAME: {
                    new GetWeatherDetails().getDataByCityname(location, new WebserviceCallBack() {
                        ProgressDialog pd;

                        @Override
                        public void showProgress() {
                            pd = new ProgressDialog(getActivity());
                            pd.setMessage("Loading...");
                            pd.show();
                        }

                        @Override
                        public void onSuccess(Object response) {
                            if (pd.isShowing()) pd.cancel();

                            WeatherData message = (WeatherData) response;
                            populateData(message, rootView);
                        }

                        @Override
                        public void onError(String errorCode) {
                            if (pd.isShowing())
                                pd.cancel();
                        }

                        @Override
                        public void onFailed(String response) {
                            if (pd.isShowing())
                                pd.cancel();
                        }

                    });
                    break;
                }
                case BY_LAT_LONG: {
                    String lat = location.split(",")[0];
                    String lon = location.split(",")[1];
                    new GetWeatherDetails().getDataByCoordinates(lat, lon, new WebserviceCallBack() {

                        @Override
                        public void showProgress() {
//
                        }

                        @Override
                        public void onSuccess(Object response) {
                            if (mProgressDialog != null) {
                                mProgressDialog.dismiss();
                            }

                            WeatherData message = (WeatherData) response;
                            populateData(message, rootView);
                        }

                        @Override
                        public void onError(String errorCode) {
                            if (mProgressDialog != null) {
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailed(String response) {
                            if (mProgressDialog != null) {
                                mProgressDialog.dismiss();
                            }
                        }

                    });
                    break;
                }
                case BY_ZIP_CODE: {
                    String zip = location.split(",")[0];
                    String country = location.split(",")[1];
                    if (country == null || country == "")
                        country = "us";
                    zip = zip + "," + country;
                    new GetWeatherDetails().getDataByZip(zip, new WebserviceCallBack() {
                        ProgressDialog pd;

                        @Override
                        public void showProgress() {
                            pd = new ProgressDialog(getActivity());
                            pd.setMessage("Loading...");
                            pd.show();
                        }

                        @Override
                        public void onSuccess(Object response) {
                            if (pd.isShowing()) pd.cancel();

                            WeatherData message = (WeatherData) response;
                            populateData(message, rootView);
                        }

                        @Override
                        public void onError(String errorCode) {
                            if (pd.isShowing())
                                pd.cancel();
                        }

                        @Override
                        public void onFailed(String response) {
                            if (pd.isShowing())
                                pd.cancel();
                        }

                    });
                    break;
                }


            }
        }
    }
    public static String getDate(long milliSeconds)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy hh:mm a");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds* 1000L);
        return formatter.format(calendar.getTime());
    }

    public static String getDateYear(long milliSeconds)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds* 1000L);
        return formatter.format(calendar.getTime());
    }
    public static String getDateDay(long milliSeconds)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM HH:mm");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds* 1000L);
        return formatter.format(calendar.getTime());
    }
    private void populateData(WeatherData data,View rootView){
        TextView city = (TextView) rootView.findViewById(R.id.city);
        TextView day = (TextView) rootView.findViewById(R.id.day);


         weatherView = (RecyclerView) rootView.findViewById(R.id.weatherlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        weatherView.setLayoutManager(mLayoutManager);
        weatherView.setItemAnimator(new DefaultItemAnimator());
         setUpItemTouchHelper();



        TextView weather = (TextView) rootView.findViewById(R.id.weather);

        TextView temp = (TextView) rootView.findViewById(R.id.temperature);
        TextView tempMax = (TextView) rootView.findViewById(R.id.txtMax);
        TextView tempMin = (TextView) rootView.findViewById(R.id.txtMin);
        TextView preci = (TextView) rootView.findViewById(R.id.preci);

        TextView humidity = (TextView) rootView.findViewById(R.id.humidity);

        TextView wind = (TextView) rootView.findViewById(R.id.wind);

        weather.setText(data.getForecast().get(0).getWeather().get(0).getDescription());
        city.setText(data.getCity().getName() +", "+data.getCity().getCountry());

        double temperatureConverted = Math.round(data.getForecast().get(0).getMain().getTemp()+Kelvin_to_degree);
        temp.setText(Html.fromHtml(""+temperatureConverted + DEGREE +"C" ));
        preci.setText("Cloudiness "+data.getForecast().get(0).getClouds().getAll());
        wind.setText("Wind "+data.getForecast().get(0).getWind().getSpeed() +" mph ");
        humidity.setText("Humidity "+data.getForecast().get(0).getMain().getHumidity()+"%");
        day.setText(getDate(data.getForecast().get(0).getDt()));
        tempMax.setText("Max " + convertTemperature(data.getForecast().get(0).getMain().getTempMax()));
        tempMin.setText("Min " + convertTemperature(data.getForecast().get(0).getMain().getTempMin()));

        if(data.getForecast()!=null) {

            String humidity1 ;
            String icon ;
            String time ;
            String windy ;
            String temperature;
            String precip;
            String checkDate=null;
            java.util.List<WeatherDescription> weathers = new ArrayList<WeatherDescription>();

            java.util.List<Forecast> weatherForecastList = data.getForecast();

            if (weatherForecastList != null) {
                for (Forecast forecast : weatherForecastList) {
                    if (checkDate == null || !(checkDate == (getDateYear(forecast.getDt())))) {
                        checkDate = getDateYear(forecast.getDt());
                        humidity1 = "Humidity " + forecast.getMain().getHumidity() + "%";
                        precip = "Cloudiness " + forecast.getClouds().getAll();
//                        icon = forecast.getWeather().get(0).getDescription();
                        time = getDateDay(forecast.getDt());
                        windy = "Wind " + forecast.getWind().getSpeed() + " mph ";
//                        double temperatureConvert = Math.round(forecast.getMain().getTemp() + Kelvin_to_degree);
                        temperature = convertTemperature(forecast.getMain().getTemp());
                        WeatherDescription desc = new WeatherDescription();
                        desc.setHumidity(humidity1);
//                        desc.setIcon(icon);
                        desc.setRain_description(precip);
                        desc.setTemperature(temperature);
                        desc.setTime(time);
                        desc.setWindy(windy);
                        desc.setMinTemp(convertTemperature(forecast.getMain().getTempMin()));
                        desc.setMaxTemp(convertTemperature(forecast.getMain().getTempMax()));
                        desc.setCloud(forecast.getWeather().get(0).getDescription());
                        weathers.add(desc);

                    }
                }
            }


            WeatherAdapter weatherAdapter = new WeatherAdapter(getActivity(), weathers);
            weatherView.setAdapter(weatherAdapter);
            weatherAdapter.notifyDataSetChanged();
        }

    }

    private String convertTemperature(float temp) {
        double temperatureConvert = Math.round(temp + Kelvin_to_degree);
        return Html.fromHtml("" + temperatureConvert + DEGREE + "C").toString();
    }

    private void setUpItemTouchHelper() {

        ItemTouchHelper SimpleItemTouchHelperCallback = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        SimpleItemTouchHelperCallback.attachToRecyclerView(weatherView);

    }


    private class WeatherAdapter extends RecyclerView.Adapter

    {

        java.util.List<WeatherDescription> weatherList;
        Activity context;


        public WeatherAdapter(Activity context, java.util.List<WeatherDescription> weatherList) {
            this.weatherList = weatherList;
            this.context = context;

        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WeatherAdapter.WeatherViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            final WeatherAdapter.WeatherViewHolder viewHolder = (WeatherAdapter.WeatherViewHolder) holder;
            final WeatherDescription weatherDescription = weatherList.get(position);
            viewHolder.tempMax.setText(weatherDescription.getTemperature());
//            viewHolder.tempMin.setText(weatherDescription.getMinTemp());
            viewHolder.txtCloud.setText(weatherDescription.getCloud());
//                viewHolder.humidity.setText(weatherDescription.getHumidity());
                viewHolder.time.setText(weatherDescription.getTime());
                viewHolder.wind.setText(weatherDescription.getWindy());
//                viewHolder.preci.setText(weatherDescription.getRain_description());
//            viewHolder.weather.setText(weatherDescription.getIcon());

        }







        @Override
        public int getItemCount() {

            return weatherList.size();
        }


        class WeatherViewHolder extends RecyclerView.ViewHolder {
            TextView tempMax;
            TextView tempMin;
            TextView txtCloud;
            TextView humidity;
            TextView weather;
            TextView time;
            TextView preci;
            TextView wind;

            public WeatherViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item_row, parent, false));
                tempMax = (TextView) itemView.findViewById(R.id.txtMaxTemp);
                tempMin = (TextView) itemView.findViewById(R.id.txtMinTemp);
//                humidity = (TextView) itemView.findViewById(R.id.humidity);
//                weather = (TextView) itemView.findViewById(R.id.weather);
                time = (TextView) itemView.findViewById(R.id.txtDate);
//                preci = (TextView) itemView.findViewById(R.id.preci);
                wind = (TextView) itemView.findViewById(R.id.txtWind);
                txtCloud = (TextView) itemView.findViewById(R.id.txtCloud);

            }


        }

    }



}


