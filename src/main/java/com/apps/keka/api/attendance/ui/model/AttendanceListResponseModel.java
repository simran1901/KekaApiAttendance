package com.apps.keka.api.attendance.ui.model;

import java.sql.Date;
import java.util.List;

public class AttendanceListResponseModel {
    Date from;
    Date to;
    List<AttendanceResponseModel> attendanceList;

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

    public List<AttendanceResponseModel> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<AttendanceResponseModel> attendanceList) {
        this.attendanceList = attendanceList;
    }
}
