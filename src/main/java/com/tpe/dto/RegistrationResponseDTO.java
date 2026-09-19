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

    // Event information
    private String eventCode;

    private String eventName;

    private LocalDate eventDate;

    private LocalTime eventTime;

    private String eventAddress;
}