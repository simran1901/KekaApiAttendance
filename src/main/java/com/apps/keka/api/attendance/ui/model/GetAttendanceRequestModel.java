package com.apps.keka.api.attendance.ui.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.sql.Date;

public class GetAttendanceRequestModel {
    @NotNull(message = "date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date from;
    @NotNull(message = "date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date to;

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
