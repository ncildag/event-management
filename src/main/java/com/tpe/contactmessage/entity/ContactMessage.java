package com.tpe.contactmessage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_contact_message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor //Hibernate kullaniyor, nesne olusturmak icin kullanir, JSON gelen veriyi, Java objesine donusturmek icin
@Builder(toBuilder = true)
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false) //BP: Bir verinin id'si olusturulduktan sonra guncellenemez
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String subject;

    @Column(nullable = false, length = 1000)
    private String message;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTime;

}