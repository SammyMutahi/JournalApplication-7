package com.example.mutahi.journalapp;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EntryListActivity extends AppCompatActivity  {
    EditText description;
    DatabaseReference mDbReference;
    List<Event> events;
    public static String descriptionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);
        events = new ArrayList<Event>();
        description = (EditText) findViewById(R.id.description);
        mDbReference = FirebaseDatabase.getInstance().getReference("events");
        AddingActionNavigationBar();
    }

    //Adding a navigation bar to navigate back to Entry list activity
    private void AddingActionNavigationBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //inflating Menu Items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_entries, menu);
        return true;
    }

    //Responding to clicks made on menuItems
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            //saving users description in Firebase Database
            case R.id.save:
                String descriptionEvent = description.getText().toString();
                //checking if the description is Empty
                if (TextUtils.isEmpty(descriptionId)) {
                    //Save
                    String id = mDbReference.push().getKey();
                    Event event = new Event(id, descriptionEvent);
                    mDbReference.child(id).setValue(event);
                    //Notify the user to show that the description entered has been saved
                    Toast.makeText(EntryListActivity.this, "Your Event has been saved Succefully", Toast.LENGTH_SHORT).show();
                }
                //Make the Description to be left null so that the user can add another event
                description.setText(" ");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
