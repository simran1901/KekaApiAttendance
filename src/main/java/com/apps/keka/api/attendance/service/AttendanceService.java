package com.apps.keka.api.attendance.service;

import com.apps.keka.api.attendance.shared.AttendanceDto;
import com.apps.keka.api.attendance.ui.model.response.AttendanceResponseModel;
import com.apps.keka.api.attendance.ui.model.request.GetAttendanceRequestModel;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface AttendanceService {
    AttendanceDto markAttendance(AttendanceDto details) throws Exception;

    Date getCurrentDate();

    Time getCurrentTime();

    void checkIn(String userId) throws Exception;

    void checkOut(String userId) throws Exception;

    public List<AttendanceResponseModel> getAttendance(String userId, GetAttendanceRequestModel details);

    public String getUserIdFromToken(String authHeader);

}
