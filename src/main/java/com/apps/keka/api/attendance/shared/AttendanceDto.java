package com.apps.keka.api.attendance.shared;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class AttendanceDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -6223532417097864400L;

    private Date date;
    private Time checkIn;
    private Time checkOut;
    private String userId;
    private boolean marked;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
}
