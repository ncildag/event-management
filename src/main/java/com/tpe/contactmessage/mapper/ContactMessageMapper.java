package com.tpe.contactmessage.mapper;

import com.tpe.contactmessage.dto.ContactMessageRequest;
import com.tpe.contactmessage.dto.ContactMessageResponse;
import com.tpe.contactmessage.entity.ContactMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ContactMessageMapper {

    //ODEV: Mapstruct kullanimi

    //Kullanicidan gelen form verisini (request), db'ye katdedilecek olan kalici entity nesnesine donusturme

    public ContactMessage requestToContactMessage(ContactMessageRequest contactMessageRequest) {
        return ContactMessage.builder()
                .name(contactMessageRequest.getName())
                .subject(contactMessageRequest.getSubject())
                .message(contactMessageRequest.getMessage())
                .email(contactMessageRequest.getEmail())
                .build();
    }

    //DB'den gelen entity nesnesini, Admin veya kullaniciya gosterecegimiz response

    public ContactMessageResponse contactMessageToResponse(ContactMessage contactMessage) {
        return ContactMessageResponse.builder()
                .id(contactMessage.getId())
                .name(contactMessage.getName())
                .subject(contactMessage.getSubject())
                .message(contactMessage.getMessage())
                .email(contactMessage.getEmail())
                .dateTime(contactMessage.getDateTime())
                .build();
    }
}