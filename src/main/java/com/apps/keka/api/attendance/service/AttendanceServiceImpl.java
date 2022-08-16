package com.apps.keka.api.attendance.service;

import com.apps.keka.api.attendance.data.AttendanceEntity;
import com.apps.keka.api.attendance.data.AttendanceRepository;
import com.apps.keka.api.attendance.data.UsersServiceClient;
import com.apps.keka.api.attendance.shared.AttendanceDto;
import com.apps.keka.api.attendance.ui.model.AttendanceResponseModel;
import com.apps.keka.api.attendance.ui.model.GetAttendanceRequestModel;
import io.jsonwebtoken.Jwts;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {
    AttendanceRepository attendanceRepository;
    UsersServiceClient usersServiceClient;
    Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, UsersServiceClient usersServiceClient,
                                 Environment environment) {
        this.attendanceRepository = attendanceRepository;
        this.environment = environment;
        this.usersServiceClient = usersServiceClient;
    }

    @Override
    public AttendanceDto markAttendance(AttendanceDto details) throws Exception {

        AttendanceDto returnValue;
        AttendanceEntity attendanceEntity = attendanceRepository.findByUserIdAndDate(details.getUserId(), details.getDate());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        if (attendanceEntity != null) {
            if (details.isMarked())
                throw new Exception("Attendance already marked");
            else {
                attendanceRepository.deleteByUserIdAndDate(details.getUserId(), details.getDate());
            }
        } else {
            if (details.isMarked()) {
                details.setCheckIn(Time.valueOf("09:30:00"));
                details.setCheckOut(Time.valueOf("18:30:00"));

                attendanceEntity = modelMapper.map(details, AttendanceEntity.class);
                attendanceRepository.save(attendanceEntity);
            } else {
                throw new Exception("Attendance already not marked");
            }
        }

        returnValue = modelMapper.map(attendanceEntity, AttendanceDto.class);
        returnValue.setMarked(details.isMarked());
        return returnValue;
    }

    @Override
    public Date getCurrentDate() {
        java.util.Date currentDate = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(currentDate);
        return Date.valueOf(date);
    }

    @Override
    public Time getCurrentTime() {
        java.util.Date currentDate = new java.util.Date();
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");

        String time = simpleTimeFormat.format(currentDate);
        return Time.valueOf(time);
    }

    @Override
    public void checkIn(String userId) throws Exception {
        // if already checked in for today
        // get attendance

        Date today = getCurrentDate();
        AttendanceEntity attendanceEntity = attendanceRepository.findByUserIdAndDate(userId, today);
        if (attendanceEntity == null) {
            attendanceEntity = new AttendanceEntity();
            attendanceEntity.setUserId(userId);
            attendanceEntity.setDate(today);
            attendanceEntity.setCheckIn(getCurrentTime());
            attendanceRepository.save(attendanceEntity);
        } else {
            throw new Exception("Already checked in");
        }

    }

    @Override
    public void checkOut(String userId) throws Exception {
        // if already checked out for today
        // get attendance

        AttendanceEntity attendanceEntity = attendanceRepository.findByUserIdAndDate(userId, getCurrentDate());
        if (attendanceEntity == null) {
            throw new Exception("checkin");
        } else {
            if (attendanceEntity.getCheckOut() == null) {
                attendanceEntity.setCheckOut(getCurrentTime());
                attendanceRepository.save(attendanceEntity);
            } else {
                throw new Exception("checkout");
            }
        }

    }

    @Override
    public List<AttendanceResponseModel> getAttendance(String userId, GetAttendanceRequestModel details) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<AttendanceEntity> entityList = attendanceRepository.findByUserIdInDuration(userId, details.getFrom(), details.getTo());
        return entityList.stream().map(p -> modelMapper.map(p, AttendanceResponseModel.class)).collect(Collectors.toList());
    }

    @Override
    public String getUserIdFromToken(String authHeader) {
        String jwt = authHeader.replace("Bearer", "");
        String userId;

        try {

            userId = Jwts.parser().setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(jwt).getBody()
                    .getSubject();
        } catch (Exception ex) {
            return null;
        }

        return userId;

    }

}
