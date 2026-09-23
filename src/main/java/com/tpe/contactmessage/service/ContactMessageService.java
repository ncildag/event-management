package com.tpe.contactmessage.service;

import com.tpe.contactmessage.dto.ContactMessageRequest;
import com.tpe.contactmessage.dto.ContactMessageResponse;
import com.tpe.contactmessage.entity.ContactMessage;
import com.tpe.contactmessage.mapper.ContactMessageMapper;
import com.tpe.contactmessage.messages.Messages;
import com.tpe.contactmessage.repository.ContactMessageRepository;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;

    //----Save new message----
    public ResponseMessage<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest) {

        //Gelen ham DTO'yu (request), db'nin anlayacagi entity formatina cevir
        final ContactMessage contactMessage = contactMessageMapper.requestToContactMessage(contactMessageRequest);

        //---2.kayit. entity'i db'ye kaydet
        final ContactMessage savedData = contactMessageRepository.save(contactMessage);

        //3.Kaydedilen veriyi (DTO olarak) yanina basarili mesaji ile return edelim
        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact Message Created Successfull")
                .httpStatus(HttpStatus.CREATED) //201
                .object(contactMessageMapper.contactMessageToResponse(savedData))
                .build();
    }

    //---Return all messages---
    public Page<ContactMessageResponse> getAll(Pageable pageable) {
        return contactMessageRepository.findAll(pageable).map(contactMessageMapper::contactMessageToResponse);
    }

    //---By email---
    public Page<ContactMessageResponse> searchByEmail(String email, Pageable pageable) {
        return contactMessageRepository.findByEmail(email, pageable).map(contactMessageMapper::contactMessageToResponse);
    }

    //---By subject---
    public Page<ContactMessageResponse> searchBySubject(String subject, Pageable pageable) {
        return contactMessageRepository.findBySubject(subject, pageable).map(contactMessageMapper::contactMessageToResponse);
    }

    //---By date---
    public List<ContactMessageResponse> searchByDateBetween(String beginDateString, String endDateString) {

        try {

            LocalDate beginDate = LocalDate.parse(beginDateString);
            LocalDate endDate = LocalDate.parse(endDateString);

            LocalDateTime startDateTime = beginDate.atStartOfDay(); // o gunun sabah 00:00:00'i
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // o gunun gece 23:59:59'i

            List<ContactMessage> rawMessages = contactMessageRepository.findByDateTimeBetween(startDateTime, endDateTime);

            //Stream API kullanarak List<Entity> 'i List<DTO> 'ya cevirelim
            return rawMessages.stream().
                    map(contactMessageMapper::contactMessageToResponse).
                    collect(Collectors.toList());

        } catch (DateTimeParseException e) {
            throw new IllegalStateException(Messages.WRONG_DATE_FORMAT);
        }
    }

    //---Delete messages by id
    public String deleteById(Long id) {
        getContactMessageById(id);
        contactMessageRepository.deleteById(id);
        return Messages.CONTACT_MESSAGE_DELETED_SUCCESSFULLY;
    }

    //---Helper by id find a message--
    public ContactMessage getContactMessageById(Long id) {
        return contactMessageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Messages.NOT_FOUND_MESSAGE)
        );
    }

    //---Controller'in Entity gormemesi icin kullanilacak metot---
    public ContactMessageResponse getContactMessageResponseById(Long id) {
        ContactMessage contactMessage = getContactMessageById(id);
        return contactMessageMapper.contactMessageToResponse(contactMessage);
    }

}