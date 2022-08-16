package com.apps.keka.api.attendance.service;

import com.apps.keka.api.attendance.shared.AttendanceDto;
import com.apps.keka.api.attendance.ui.model.AttendanceResponseModel;
import com.apps.keka.api.attendance.ui.model.GetAttendanceRequestModel;
import io.jsonwebtoken.Jwts;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface AttendanceService {
    AttendanceDto markAttendance(AttendanceDto details);

    Date getCurrentDate();

    Time getCurrentTime();

    void checkIn(String userId);

    void checkOut(String userId);

    public List<AttendanceResponseModel> getAttendance(String userId, GetAttendanceRequestModel details);

    public String getUserIdFromToken(String authHeader);

}
