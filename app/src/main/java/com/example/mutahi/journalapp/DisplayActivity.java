package com.example.mutahi.journalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ListView mListView;
    FirebaseListAdapter<Event> mAdapter;
    DatabaseReference mDbReference;
    List<Event> events;
    DrawerLayout drawer;
    EventAdapter mvAdapter;
    private String descriptionId;
    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        mListView = (ListView) findViewById(R.id.list_view);
        //Storing Users description from the Db in events
        events=new ArrayList<>();
        mvAdapter=new EventAdapter(this);
        //getting access to FirebaseDatabase
        mDbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://journalapp-challenge.firebaseio.com/Event");
        DisplayEvents();

        //Adding Navigation ToolBar
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,
        R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
       //Implementing Navigation
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    //Handling BackPress from Navigation to Display Activity

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    //Displaying Events
    public void DisplayEvents(){
        mAdapter=new FirebaseListAdapter<Event>(
                this,
                Event.class,
                android.R.layout.activity_list_item,
                mDbReference
        ) {
            @Override
            protected void populateView(View v, Event model, int position) {
                TextView description = v.findViewById(R.id.display_events);
                description.setText((CharSequence) model);
            }


        };
        mListView.setAdapter(mAdapter);
    }

    //handling device Rotation

    @Override
    protected void onResume() {
        super.onResume();
        DisplayEvents();
    }
   //Incase of any changes in the Database occuring the UI should be updated accordingly
   @Override
   //onStart is automatically called whenever our app launches
   protected void onStart() {
       super.onStart();
//Adding Listener for the Database
       mDbReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                   events.clear();
                   Event event = postSnapshot.getValue(Event.class);
                   events.add(event);
                   DisplayActivity.this.notify();
               }

           }
         //Handles events associated with cancellation
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
   }
   //Inflating menu Items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_entries,menu);
        return true;
    }
    //Responding to clicks made on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.entry_list:
                startActivity(new Intent(DisplayActivity.this, EntryListActivity.class));
                return true;
            case R.id.updating_events:
                //updating an event when a list view at an index is clicked
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        UpdateEvents();

                    }
                });
                return true;
            case R.id.deleting_events:
                //performing an action when delete events is clicked
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        deleteEvents();


                    }
                });
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    //Updating Events
    public void UpdateEvents(){
        String description = mvAdapter.description.getText().toString();
        if(!TextUtils.isEmpty(descriptionId)){
            mDbReference.child(descriptionId).child("description").setValue(description);
            Toast.makeText(DisplayActivity.this, "Your Event has been Updated Succefully", Toast.LENGTH_SHORT).show();
            mvAdapter.description.setText(event.getDescription());
            descriptionId = event.getId();
        }
    }
    public void deleteEvents(){
        mDbReference.child(event.getId()).removeValue();
    }
  //Responding to clicks made on navigation items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.location:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Location()).commit();
                break;
            case R.id.entries:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EntriesFragment()).commit();
                break;
            case R.id.update_event:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UpdateFragment()).commit();
                break;
            case R.id.delete_event:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DeleteFragment()).commit();
            default:

                return true;
        }
        return true;
    }
}