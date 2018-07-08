package com.example.mutahi.journalapp;


import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Location extends android.support.v4.app.Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.location_fragment,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapLocation();
        //Handling navigation
        if(savedInstanceState != null){
            mapLocation();
        }
    }
    //getting access to a map location
    public void mapLocation(){
        String addressString = "1600 Aphitheatre Parkway,CA";
        Uri.Builder builder= new Uri.Builder();
        builder.scheme("geo")
                .path("0,0")
                .query(addressString);
        Uri addressUri = builder.build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(addressUri);
        startActivity(intent);
    }
     //handling device rotation
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapLocation();
    }
}
