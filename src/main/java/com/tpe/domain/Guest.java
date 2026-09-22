package com.tpe.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "guests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Indicates whether this guest is a child under 10 years old.
    @Column(nullable = false)
    private Boolean child = false;

    // Indicates whether this child is under 3 years old.
    // If underThree is true, child must also be true.
    @Column(nullable = false)
    private Boolean underThree = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id")
    private Registration registration;

}