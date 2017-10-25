package com.android.weatherforecast.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anu on 25/10/2017.
 */


public class Snow {

    @SerializedName("3h")
    @Expose
    private Float _3h;

    public Float get3h() {
        return _3h;
    }

    public void set3h(Float _3h) {
        this._3h = _3h;
    }

}
