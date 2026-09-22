package com.tpe.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import com.tpe.domain.enums.EventStatus;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String eventCode;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer numberOfAttendees;

    @Column(columnDefinition = "TEXT")
    private String importantInformation;

    @Column(nullable = false)
    private Boolean veganOptionEnabled = true;

    // Determines whether the "child under 10 years old" question
    // is shown for guests on the registration form.
    @Column(nullable = false)
    private Boolean askChild = false;

    // Determines whether the "under 3 years old" question
    // is shown for child guests on the registration form.
    @Column(nullable = false)
    private Boolean askUnderThree = false;

    @Column(nullable = false)
    private LocalDateTime finalAcceptanceDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status = EventStatus.OPEN;

    @OneToMany(mappedBy = "event")
    private List<Registration> registrations;


}