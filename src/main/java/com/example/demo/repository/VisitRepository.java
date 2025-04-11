package com.example.demo.repository;

import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface VisitRepository extends JpaRepository<Visit, Long> {
    Page<Visit> findAll(Pageable pageable);

    @Query("""
    SELECT v FROM Visit v
    WHERE v.doctor = :doctor
    AND ((v.startDateTime < :endDateTime AND v.endDateTime > :startDateTime)
    OR (v.startDateTime = :endDateTime OR v.endDateTime = :startDateTime)
    OR (v.startDateTime BETWEEN :startDateTime AND :endDateTime)
    OR (v.endDateTime BETWEEN :startDateTime AND :endDateTime))
    """)
    List<Visit> findOverlappingVisits(
            @Param("doctor") Doctor doctor,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    @Query("""
    SELECT v FROM Visit v
    WHERE v.doctor = :doctor
    AND (ABS(TIMESTAMPDIFF(MINUTE, v.endDateTime, :startDateTime)) < 15
    OR ABS(TIMESTAMPDIFF(MINUTE, v.startDateTime, :endDateTime)) < 15)
    """)
    List<Visit> findVisitsViolatingBreak(
            @Param("doctor") Doctor doctor,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    Page<Visit> findByPatientEmail(String patientEmail, Pageable pageable);


}
