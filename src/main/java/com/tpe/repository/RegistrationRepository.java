package com.tpe.repository;

import com.tpe.domain.Registration;
import com.tpe.domain.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository
        extends JpaRepository<Registration, Long> {

    List<Registration> findByEmail(String email);

    List<Registration> findByEventId(Long eventId);

    Optional<Registration> findByRegistrationCode(String registrationCode);

    boolean existsByRegistrationCode(String registrationCode);

    @Query("""
           SELECT COALESCE(SUM(r.totalRegistration), 0)
           FROM Registration r
           WHERE r.event.id = :eventId
           AND r.status = com.tpe.domain.enums.RegistrationStatus.ACTIVE
           """)
    Long getTotalRegistrationByEventId(@Param("eventId") Long eventId);

    boolean existsByEmailAndEventIdAndStatus(
            String email,
            Long eventId,
            RegistrationStatus status
    );

    boolean existsByEmailAndEventIdAndStatusAndIdNot(
            String email,
            Long eventId,
            RegistrationStatus status,
            Long id
    );

    long countByEventIdAndStatus(
            Long eventId,
            RegistrationStatus status
    );


    // -----------------------------------------
    // RESET VEGAN INFORMATION
    // -----------------------------------------

    @Modifying
    @Query("""
           UPDATE Registration r
           SET r.veganMenuCount = 0,
               r.veganAttendeeNames = null
           WHERE r.event.id = :eventId
           """)
    int resetVeganInformationByEventId(
            @Param("eventId") Long eventId
    );

}