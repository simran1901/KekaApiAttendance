package com.apps.keka.api.attendance.service;

import com.apps.keka.api.attendance.data.AttendanceEntity;
import com.apps.keka.api.attendance.data.AttendanceRepository;
import com.apps.keka.api.attendance.data.UsersServiceClient;
import com.apps.keka.api.attendance.ui.model.response.UserResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class AttendanceMonitor {

    AttendanceRepository attendanceRepository;
    UsersServiceClient usersServiceClient;
    Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AttendanceMonitor(AttendanceRepository attendanceRepository, UsersServiceClient usersServiceClient,
                             Environment environment) {
        this.attendanceRepository = attendanceRepository;
        this.environment = environment;
        this.usersServiceClient = usersServiceClient;
    }

    @Scheduled(cron = "0 31 18 * * ?")
    public void markFullAttendance() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<UserResponseModel> usersList = usersServiceClient.getUsers().getBody();
        List<String> userIds = usersList.stream().map(p -> p.getUserId()).toList();

        java.util.Date currentDate = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(environment.getProperty("date.format"));
        String date = simpleDateFormat.format(currentDate);
        Date today = Date.valueOf(date);

        // for current date
        // if present in database but checkin not null and check out null
        // then update checkout to 6:30 pm
        for (String userId : userIds) {
            AttendanceEntity attendanceEntity = attendanceRepository.findByUserIdAndDate(userId, today);
            if (attendanceEntity == null) {
                attendanceEntity = new AttendanceEntity();
                attendanceEntity.setUserId(userId);
                attendanceEntity.setDate(today);
                attendanceRepository.save(attendanceEntity);
            } else if (attendanceEntity.getCheckOut() == null && attendanceEntity.getCheckIn() != null) {
                attendanceEntity.setCheckOut(Time.valueOf(environment.getProperty("checkout.time")));
                attendanceRepository.save(attendanceEntity);
            }
        }
        logger.info("Database was successfully updated");

        // if not present in database, add to database with null values
    }

}
