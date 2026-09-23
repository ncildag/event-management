package com.tpe.contactmessage.controller;

import com.tpe.contactmessage.dto.ContactMessageRequest;
import com.tpe.contactmessage.dto.ContactMessageResponse;
import com.tpe.contactmessage.service.ContactMessageService;
import com.tpe.payload.response.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact-messages")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    //---1.Yeni Mesaj Kaydetme---
    //POST http://localhost:8080/contact-messages

    @PostMapping
    public ResponseEntity<ResponseMessage<ContactMessageResponse>> saveContact(@RequestBody @Valid ContactMessageRequest contactMessageRequest) {
        ResponseMessage<ContactMessageResponse> response = contactMessageService.save(contactMessageRequest);
        //return new ResponseEntity<>(response, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //---2.Tum Mesajlari Sayfali Getirme
    //GET http://localhost:8080/contact-messages?page=0...
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ContactMessageResponse>> getAll(
            @PageableDefault(size = 10, page = 0, sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(contactMessageService.getAll(pageable));
    }

    //---3.E-postaya Gore Arama
    //GET http://localhost:8080/contact-messages/search/email?email=....
    @GetMapping("/search/email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ContactMessageResponse>> searchByEmail(
            @RequestParam(value = "email") String email,
            @PageableDefault(size = 10, page = 0, sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(contactMessageService.searchByEmail(email, pageable));
    }

    // --- 4. KONUYA (SUBJECT) GÖRE ARAMA ---
    @GetMapping("/search/subject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ContactMessageResponse>> searchBySubject(
            @RequestParam(value = "subject") String subject,
            @PageableDefault(size = 10, page = 0, sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(contactMessageService.searchBySubject(subject, pageable));
    }

    // --- 5. TARİH ARALIĞINA GÖRE ARAMA ---
    @GetMapping("/search/date-between")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactMessageResponse>> searchByDateBetween(
            @RequestParam(value = "beginDate") String beginDateString,
            @RequestParam(value = "endDate") String endDateString){

        List<ContactMessageResponse> contactMessages = contactMessageService.searchByDateBetween(beginDateString, endDateString);
        return ResponseEntity.ok(contactMessages);
    }

    // --- 6. ID İLE GETİRME (SEKTÖR STANDARDI) ---
    // BEST PRACTICE: "/getById/{id}" yazılmaz. Tek bir kaynağa ulaşıyorsak direkt "/{id}" yazılır.
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContactMessageResponse> getById(@PathVariable("id") Long id){

        return ResponseEntity.ok(contactMessageService.getContactMessageResponseById(id));
    }

    // --- 7. ID İLE SİLME (SEKTÖR STANDARDI) ---
    // BEST PRACTICE: "/deleteById/{id}" yazılmaz. İşlemi yapan zaten DELETE metodudur, doğrudan "/{id}" yazılır.
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id){
        return ResponseEntity.ok(contactMessageService.deleteById(id));
    }

}
