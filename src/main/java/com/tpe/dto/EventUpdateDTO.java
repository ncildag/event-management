package com.tpe.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class EventUpdateDTO {

    private String eventName;

    private LocalDate date;

    private LocalTime time;

    private String address;

    private String importantInformation;

    private Boolean veganOptionEnabled;

    // Determines whether the child question is enabled for this event.
    private Boolean askChild;

    // Determines whether the under 3 question is enabled for this event.
    private Boolean askUnderThree;

    private Integer numberOfAttendees;

    private LocalDateTime finalAcceptanceDate;

}