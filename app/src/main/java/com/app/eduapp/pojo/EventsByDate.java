package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsByDate {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("Message")
    @Expose
    public String Message;

    @SerializedName("data")
    @Expose
    public Datum data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Datum getData() {
        return data;
    }

    public void setData(Datum data) {
        this.data = data;
    }

    public class Datum {
        @SerializedName("EventsList")
        @Expose
        public List<EventsList> EventsList;

        public List<EventsByDate.EventsList> getEventsList() {
            return EventsList;
        }

        public void setEventsList(List<EventsByDate.EventsList> eventsList) {
            EventsList = eventsList;
        }
    }

    public class EventsList {
        @SerializedName("EventsID")
        @Expose
        public String EventsID;

        @SerializedName("EventsTitle")
        @Expose
        public String EventsTitle;


        @SerializedName("EventsLocation")
        @Expose
        public String EventsLocation;

        @SerializedName("EventsDescription")
        @Expose
        public String EventsDescription;

        @SerializedName("EventsStartDate")
        @Expose
        public String EventsStartDate;

        @SerializedName("EventsEndsDate")
        @Expose
        public String EventsEndsDate;

        @SerializedName("EventsStartTime")
        @Expose
        public String EventsStartTime;

        @SerializedName("EventsEndsTime")
        @Expose
        public String EventsEndsTime;

        public String getEventsID() {
            return EventsID;
        }

        public void setEventsID(String eventsID) {
            EventsID = eventsID;
        }

        public String getEventsTitle() {
            return EventsTitle;
        }

        public void setEventsTitle(String eventsTitle) {
            EventsTitle = eventsTitle;
        }

        public String getEventsLocation() {
            return EventsLocation;
        }

        public void setEventsLocation(String eventsLocation) {
            EventsLocation = eventsLocation;
        }

        public String getEventsDescription() {
            return EventsDescription;
        }

        public void setEventsDescription(String eventsDescription) {
            EventsDescription = eventsDescription;
        }

        public String getEventsStartDate() {
            return EventsStartDate;
        }

        public void setEventsStartDate(String eventsStartDate) {
            EventsStartDate = eventsStartDate;
        }

        public String getEventsEndsDate() {
            return EventsEndsDate;
        }

        public void setEventsEndsDate(String eventsEndsDate) {
            EventsEndsDate = eventsEndsDate;
        }

        public String getEventsStartTime() {
            return EventsStartTime;
        }

        public void setEventsStartTime(String eventsStartTime) {
            EventsStartTime = eventsStartTime;
        }

        public String getEventsEndsTime() {
            return EventsEndsTime;
        }

        public void setEventsEndsTime(String eventsEndsTime) {
            EventsEndsTime = eventsEndsTime;
        }
    }

}
