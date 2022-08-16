package com.apps.keka.api.attendance.ui.controllers;

import com.apps.keka.api.attendance.data.UsersServiceClient;
import com.apps.keka.api.attendance.service.AttendanceService;
import com.apps.keka.api.attendance.shared.AttendanceDto;
import com.apps.keka.api.attendance.ui.model.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    UsersServiceClient usersServiceClient;
    @Autowired
    AttendanceService attendanceService;
    @Autowired
    private Environment env;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port " + env.getProperty("server.port") + ", with token = " + env.getProperty("token.secret");
    }

    // mark attendance
    @PatchMapping(value = "/{userId}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AttendanceDto> markAttendance(@RequestHeader(value = "Authorization") String authHeader,
                                                        @PathVariable("userId") String userId,
                                                        @RequestBody MarkAttendanceRequestModel details) {
        if (Boolean.FALSE.equals(usersServiceClient.isAdmin(authHeader).getBody())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        AttendanceDto attendanceDto = modelMapper.map(details, AttendanceDto.class);
        attendanceDto.setUserId(userId);
        try {
            attendanceDto = attendanceService.markAttendance(attendanceDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(attendanceDto);
    }

    // check in
    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HttpStatus> checkIn(@RequestHeader(value = "Authorization") String authHeader) {
        if (Boolean.FALSE.equals(usersServiceClient.isUser(authHeader).getBody())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String userId = attendanceService.getUserIdFromToken(authHeader);

        try {
            attendanceService.checkIn(userId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }


        return new ResponseEntity<>(HttpStatus.OK);
    }

    // check out
    @PatchMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HttpStatus> checkOut(@RequestHeader(value = "Authorization") String authHeader) {
        if (Boolean.FALSE.equals(usersServiceClient.isUser(authHeader).getBody())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String userId = attendanceService.getUserIdFromToken(authHeader);
        try {
            attendanceService.checkOut(userId);
        } catch (Exception e) {
            if (e.getLocalizedMessage().equals("checkout"))
                return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // get attendance
    @GetMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AttendanceListResponseModel> getAttendance(@RequestHeader(value = "Authorization") String authHeader,
                                                                     @RequestBody GetAttendanceRequestModel details) {
        if (Boolean.FALSE.equals(usersServiceClient.isUser(authHeader).getBody())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        String userId = attendanceService.getUserIdFromToken(authHeader);
        AttendanceListResponseModel returnValue = modelMapper.map(details, AttendanceListResponseModel.class);
        returnValue.setAttendanceList(attendanceService.getAttendance(userId, details));

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

}
