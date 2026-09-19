package com.tpe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventParticipantSummaryDTO {

    private Integer capacity;

    private Long activeRegisteredPeople;

    private Integer remainingCapacity;

    private Long activeRegistrations;

    private Long cancelledRegistrations;
}