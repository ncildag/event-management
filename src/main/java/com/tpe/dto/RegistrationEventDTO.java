package com.tpe.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class RegistrationEventDTO {

    private String eventName;

    private LocalDate date;

    private LocalTime time;

    private String address;

    private String importantInformation;

    private Integer capacity;

    private Integer remainingCapacity;

    private String eventCode;

    private String status;

    private LocalDateTime finalAcceptanceDate;

}