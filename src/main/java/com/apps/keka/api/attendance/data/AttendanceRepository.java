package com.apps.keka.api.attendance.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface AttendanceRepository extends CrudRepository<AttendanceEntity, Long> {
    AttendanceEntity findByUserIdAndDate(String userId, Date date);

    void deleteByUserIdAndDate(String userId, Date date);

    @Query(value = "SELECT * FROM attendance WHERE user_id = ?1 AND date BETWEEN ?2 AND ?3 ORDER BY date ASC", nativeQuery = true)
    List<AttendanceEntity> findByUserIdInDuration(String userId, Date from, Date to);
}
