package com.tpe.service;

import com.tpe.domain.Event;
import com.tpe.dto.EventCreateDTO;
import com.tpe.dto.RegistrationEventDTO;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tpe.dto.EventUpdateDTO;
import com.tpe.domain.enums.EventStatus;
import com.tpe.repository.RegistrationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public Event createEvent(EventCreateDTO dto) {

        Event event = new Event();

        event.setEventName(dto.getEventName());
        event.setDate(dto.getDate());
        event.setTime(dto.getTime());
        event.setAddress(dto.getAddress());
        event.setImportantInformation(dto.getImportantInformation());
        event.setNumberOfAttendees(dto.getNumberOfAttendees());
        event.setFinalAcceptanceDate(dto.getFinalAcceptanceDate());

        event.setEventCode(generateEventCode());

        event.setCreatedAt(LocalDateTime.now());

        return eventRepository.save(event);
    }


    //Get all events ------------------------
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }


    //Generate event code ------------------------
    private String generateEventCode() {

        String eventCode;
        do {
            eventCode = UUID.randomUUID()
                    .toString()
                    .substring(0, 8)
                    .toUpperCase();
        } while (eventRepository.existsByEventCode(eventCode));

        return eventCode;
    }

    //Get event by ID ------------------------
    public Event getEventById(Long id) {

        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found with id: " + id
                        )
                );
    }

    //Update event ------------------------
    public Event updateEvent(Long id, EventUpdateDTO dto) {

        // 1. Find event
        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found with id: " + id
                        )
                );


        // 2. Check active registrations
        Long activeRegisteredPeople =
                registrationRepository
                        .getTotalRegistrationByEventId(id);


        // 3. Ney capacity can't be less than current active registrations
        if (dto.getNumberOfAttendees()
                < activeRegisteredPeople.intValue()) {

            throw new IllegalStateException(
                    "Event capacity cannot be lower than the number of active registered people."
            );
        }


        // 4. Update event
        event.setEventName(dto.getEventName());
        event.setDate(dto.getDate());
        event.setTime(dto.getTime());
        event.setAddress(dto.getAddress());
        event.setImportantInformation(dto.getImportantInformation());
        event.setNumberOfAttendees(dto.getNumberOfAttendees());
        event.setFinalAcceptanceDate(dto.getFinalAcceptanceDate());


        // 5. Save in db.
        return eventRepository.save(event);
    }

    //Delete event ------------------------
    public void deleteEvent(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found with id: " + id
                        )
                );

        eventRepository.delete(event);
    }

    //Close event
    public void closeEvent(Long id){

        Event event = getEventById(id);

        event.setStatus(EventStatus.CLOSED);

        eventRepository.save(event);

    }

    //Reopen event
    public void reopenEvent(Long id){

        Event event = getEventById(id);

        event.setStatus(EventStatus.OPEN);

        eventRepository.save(event);

    }

    //Get Event by Code --> for users shared link
    public Event getEventByCode(String eventCode){

        return eventRepository
                .findByEventCode(eventCode)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "Event not found."
                        ));

    }

    //Get event by code --> for users shared link - via api
    public RegistrationEventDTO getRegistrationEvent(String eventCode) {

        Event event = getEventByCode(eventCode);

        Long currentRegistration =
                registrationRepository
                        .getTotalRegistrationByEventId(event.getId());

        int remainingCapacity =
                event.getNumberOfAttendees()
                        - currentRegistration.intValue();

        RegistrationEventDTO dto = new RegistrationEventDTO();

        dto.setEventName(event.getEventName());
        dto.setDate(event.getDate());
        dto.setTime(event.getTime());
        dto.setAddress(event.getAddress());
        dto.setImportantInformation(event.getImportantInformation());
        dto.setCapacity(event.getNumberOfAttendees());
        dto.setRemainingCapacity(remainingCapacity);
        dto.setEventCode(event.getEventCode());
        dto.setStatus(event.getStatus().name());
        dto.setFinalAcceptanceDate(event.getFinalAcceptanceDate());

        return dto;
    }

}