package com.tpe.dto;

import com.tpe.domain.enums.RegistrationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RegistrationResponseDTO {

    private String registrationCode;

    private String name;

    private String email;

    private String phone;

    private Integer totalRegistration;

    private Integer veganMenuCount;

    private String veganAttendeeNames;

    private RegistrationStatus status;

    private List<GuestDTO> guests = new ArrayList<>();


    // =========================================
    // EVENT REGISTRATION OPTIONS
    // =========================================

    // Determines whether the child question
    // should be shown for guests.
    private Boolean askChild;

    // Determines whether the under 3 question
    // should be shown for child guests.
    private Boolean askUnderThree;


    // =========================================
    // EVENT INFORMATION
    // =========================================

    private String eventCode;

    private String eventName;

    private LocalDate eventDate;

    private LocalTime eventTime;

    private String eventAddress;
}