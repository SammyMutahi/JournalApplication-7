package com.example.mutahi.journalapp;


public class Event {
    private String id;
    private String description;

    //A Default Constructor
    public Event() {
    }
   //initialized constructor
    public Event(String id, String description) {
        this.id = id;
        this.description = description;
    }
 /**Getters and Setters of id and Description*/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
