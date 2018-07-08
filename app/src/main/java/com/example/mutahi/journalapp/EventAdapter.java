package com.example.mutahi.journalapp;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    private Activity context;
    private List<Event> events;
    DatabaseReference mDbReference;
    EditText description;


    public EventAdapter(@NonNull Context context) {
        super(context, R.layout.activity_list_item);
        this.context = (Activity) context;
        this.events = events;
        this.mDbReference = mDbReference;
        this.description = description;
    }
    public View getView(int pos, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_list_item,null,true);
        TextView displayDescription = (TextView) listViewItem.findViewById(R.id.display_events);
        //getting the Event position
        final Event event = events.get(pos);
        //Displaying the User's Events
        displayDescription.setText(event.getDescription());
        return listViewItem;
    }
}
