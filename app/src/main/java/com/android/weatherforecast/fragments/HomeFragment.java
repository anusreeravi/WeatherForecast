package com.android.weatherforecast.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.weatherforecast.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    View rootView;
    List<String> locationTypeList=new ArrayList<String>();
    private static final String BY_CITY_NAME = "CITY NAME ,COUNTRY CODE";
    private static final String BY_CITY_ID = "CITY ID";
    private static final String BY_LAT_LONG = "GEOGRAPHIC CO_ORDINATES";
    private static final String BY_ZIP_CODE = "ZIP CODE,COUNTRY CODE";

    private static final String EMPTY_MESSAGE="Fields are Empty.Please fill details";
    private static final String COUNTRY_CODE = "COUNTRY CODE";
    private static final String CITY_NAME = "CITY NAME";
    private static final String CITY_ID = "CITY ID";
    private static final String CITY_ZIP = "CITY ZIP";
    private static final String LAT = "LATITUDE";
    private static final String LON = "LONGITUDE";
    String locationType;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
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
   private void inflateViews(final View rootView){

       locationTypeList.add(BY_CITY_NAME);
       locationTypeList.add(BY_CITY_ID);
       locationTypeList.add(BY_LAT_LONG);
       locationTypeList.add(BY_ZIP_CODE);



       if (locationTypeList != null && locationTypeList.size() > 0) {
           String[] locatnList = new String[locationTypeList.size()];
           int i = 0;
           int position = 0;
           for (String locn : locationTypeList) {
               locatnList[i] = locn;
               i++;
           }



           ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, locatnList); //selected item will look like a spinner set from XML
           spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           final AppCompatSpinner spinner =(AppCompatSpinner) rootView.findViewById(R.id.location_layout);
           spinner.setAdapter(spinnerArrayAdapter);
           spinner.setSelection(position);
           spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    locationType =  spinner.getSelectedItem().toString();
                   switch(locationType){
                       case BY_CITY_ID:
                           ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.secondtext)).setVisibility(View.GONE);
                           ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.location)).setHint(CITY_ID);

                           break;
                       case BY_CITY_NAME:
                           ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.secondtext)).setVisibility(View.VISIBLE);
                           ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.location)).setHint(CITY_NAME);
                           ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.secondtext)).setHint(COUNTRY_CODE);

                           break;
                       case BY_LAT_LONG:
                           ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.secondtext)).setVisibility(View.VISIBLE);
                           ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.location)).setHint(LAT);
                           ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.secondtext)).setHint(LON);

                           break;
                       case BY_ZIP_CODE:
                           ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.secondtext)).setVisibility(View.VISIBLE);
                           ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.location)).setHint(CITY_ZIP);
                           ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.secondtext)).setHint(COUNTRY_CODE);
                           break;
                   }
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {
                   locationType =  BY_CITY_NAME;
                   ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.secondtext)).setVisibility(View.VISIBLE);
                   ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.location)).setHint(CITY_NAME);
                   ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.secondtext)).setHint(COUNTRY_CODE);
               }
           } );






           ((TextView)rootView.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                 //  String locationType = ((AppCompatSpinner) rootView.findViewById(R.id.location_layout)).getSelectedItem().toString();
                   String secondData = ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.secondtext)).getText().toString();
                   String location = ((android.support.design.widget.TextInputEditText) rootView.findViewById(R.id.location)).getText().toString()+","+secondData;

                   if(location == null || location=="" ||locationType==null ||locationType=="")
                     displayDialog(EMPTY_MESSAGE,getActivity());
                   else {
                      Bundle bundle = new Bundle();
                       bundle.putString("locationType",locationType);
                       bundle.putString("location",location);
                       DashboardFragment fragment = new DashboardFragment();
                       fragment.setArguments(bundle);
                       getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).disallowAddToBackStack().commit();


                   }
                   }

                   });


   }

};
       }


