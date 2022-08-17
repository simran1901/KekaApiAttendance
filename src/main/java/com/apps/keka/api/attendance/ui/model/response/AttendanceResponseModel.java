package com.apps.keka.api.attendance.ui.model.response;

import java.sql.Date;
import java.sql.Time;

public class AttendanceResponseModel {
    Date date;
    Time checkIn;
    Time checkOut;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Time checkIn) {
        this.checkIn = checkIn;
    }

    public Time getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Time checkOut) {
        this.checkOut = checkOut;
    }
}
