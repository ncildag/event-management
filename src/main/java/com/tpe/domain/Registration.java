package com.tpe.domain;

import com.tpe.domain.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kullanıcının daha sonra kendi kaydını düzenleyebilmesi için
    @Column(nullable = false, unique = true)
    private String registrationCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Integer totalRegistration;

    private Integer veganMenuCount;

    private String veganAttendeeNames;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status = RegistrationStatus.ACTIVE;

    @Column(nullable = false)
    private Boolean emailSent = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToMany(mappedBy = "registration",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Guest> guests = new ArrayList<>();

}