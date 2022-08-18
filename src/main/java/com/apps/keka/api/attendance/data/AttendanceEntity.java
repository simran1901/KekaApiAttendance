package com.apps.keka.api.attendance.data;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "attendance")
public class AttendanceEntity implements Serializable {

    private static final long serialVersionUID = 6068537671745749794L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 10)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(length = 8)
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Time checkIn;

    @Column(length = 8)
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Time checkOut;

    @Column(nullable = false)
    private String userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
}
