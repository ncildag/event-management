package com.tpe.contactmessage.repository;

import com.tpe.contactmessage.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    //Belirli e-posta adresinden gelen mesajlari filtrele
    Page<ContactMessage> findByEmail(String email, Pageable pageable);

    //Belirli bir konu basligina gore filtrele(sikayet, destek, iade)
    Page<ContactMessage> findBySubject(String subject, Pageable pageable);

    //Belirli tarih araligindaki mesajlari gormek
    List<ContactMessage> findByDateTimeBetween(LocalDateTime beginDateTime, LocalDateTime endDateTime);

}