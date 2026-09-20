package com.tpe.service;

import com.tpe.domain.Event;
import com.tpe.domain.Guest;
import com.tpe.domain.Registration;
import com.tpe.domain.enums.EventStatus;
import com.tpe.domain.enums.RegistrationStatus;
import com.tpe.dto.GuestDTO;
import com.tpe.dto.RegistrationCreateDTO;
import com.tpe.dto.RegistrationUpdateDTO;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.EventRepository;
import com.tpe.repository.GuestRepository;
import com.tpe.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tpe.dto.RegistrationResponseDTO;
import com.tpe.dto.EventParticipantSummaryDTO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RegistrationService {


    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final GuestRepository guestRepository;
    private final EmailService emailService;


    @Transactional
    public Registration createRegistration(
            String eventCode,
            RegistrationCreateDTO dto){

        Event event = eventRepository
                .findByEventCodeWithLock(eventCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found."));

        if(event.getStatus() == EventStatus.CLOSED){

            throw new IllegalStateException(
                    "This event is closed.");

        }

        if(LocalDateTime.now()
                .isAfter(event.getFinalAcceptanceDate())){

            throw new IllegalStateException(
                    "Registration deadline has passed.");

        }

        boolean alreadyRegistered =
                registrationRepository.existsByEmailAndEventIdAndStatus(
                        dto.getEmail(),
                        event.getId(),
                        RegistrationStatus.ACTIVE
                );

        if (alreadyRegistered) {
            throw new IllegalStateException(
                    "This email is already registered for this event.");
        }

        //TODO
        //Capacity Check
        Long currentTotal =
                registrationRepository.getTotalRegistrationByEventId(event.getId());

        Integer requestedTotal = dto.getTotalRegistration();

        if (currentTotal + requestedTotal > event.getNumberOfAttendees()) {
            throw new IllegalStateException(
                    "There is not enough capacity for this registration.");
        }

        Registration registration = new Registration();



        Integer veganMenuCount =
                dto.getVeganMenuCount() == null
                        ? 0
                        : dto.getVeganMenuCount();

        if (veganMenuCount > dto.getTotalRegistration()) {
            throw new IllegalStateException(
                    "Vegan menu count cannot be greater than total registration.");
        }

        if (veganMenuCount > 0 &&
                (dto.getVeganAttendeeNames() == null ||
                        dto.getVeganAttendeeNames().isBlank())) {

            throw new IllegalStateException(
                    "Please enter the names of the attendees who will have the vegan menu.");
        }

        registration.setRegistrationCode(generateRegistrationCode());
        registration.setName(dto.getName());
        registration.setEmail(dto.getEmail());
        registration.setPhone(dto.getPhone());
        registration.setTotalRegistration(dto.getTotalRegistration());
        registration.setVeganMenuCount(veganMenuCount);
        registration.setVeganAttendeeNames(dto.getVeganAttendeeNames());
        registration.setRegisteredAt(LocalDateTime.now());
        registration.setStatus(RegistrationStatus.ACTIVE);
        registration.setEvent(event);

        for (GuestDTO guestDTO : dto.getGuests()) {
            Guest guest = new Guest();
            guest.setName(guestDTO.getName());
            guest.setRegistration(registration);
            registration.getGuests().add(guest);
        }
        Registration savedRegistration =
                registrationRepository.save(registration);

        try {

            emailService.sendRegistrationConfirmation(savedRegistration);

            savedRegistration.setEmailSent(true);

        } catch (Exception e) {

            savedRegistration.setEmailSent(false);

            System.out.println(
                    "Confirmation email could not be sent: "
                            + e.getClass().getName()
                            + " - "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        registrationRepository.save(savedRegistration);

        return savedRegistration;
    }


    public RegistrationResponseDTO getRegistrationByCode(
            String registrationCode) {

        Registration registration = registrationRepository
                .findByRegistrationCode(registrationCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Registration not found."));

        RegistrationResponseDTO dto =
                new RegistrationResponseDTO();

        dto.setRegistrationCode(registration.getRegistrationCode());
        dto.setName(registration.getName());
        dto.setEmail(registration.getEmail());
        dto.setPhone(registration.getPhone());
        dto.setTotalRegistration(registration.getTotalRegistration());
        dto.setVeganMenuCount(registration.getVeganMenuCount());
        dto.setVeganAttendeeNames(registration.getVeganAttendeeNames());
        dto.setStatus(registration.getStatus());

        dto.setEventCode(registration.getEvent().getEventCode());
        dto.setEventName(registration.getEvent().getEventName());
        dto.setEventDate(registration.getEvent().getDate());
        dto.setEventTime(registration.getEvent().getTime());
        dto.setEventAddress(registration.getEvent().getAddress());

        for (Guest guest : registration.getGuests()) {

            GuestDTO guestDTO = new GuestDTO();

            guestDTO.setName(guest.getName());

            dto.getGuests().add(guestDTO);
        }

        return dto;
    }

    @Transactional
    public RegistrationResponseDTO updateRegistration(
            String registrationCode,
            RegistrationUpdateDTO dto) {

        Registration registration = registrationRepository
                .findByRegistrationCode(registrationCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Registration not found."));

        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cancelled registration cannot be updated.");
        }

        Event event = eventRepository
                .findByEventCodeWithLock(
                        registration.getEvent().getEventCode()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found."));

        boolean emailUsedByAnotherRegistration =
                registrationRepository
                        .existsByEmailAndEventIdAndStatusAndIdNot(
                                dto.getEmail(),
                                event.getId(),
                                RegistrationStatus.ACTIVE,
                                registration.getId()
                        );

        if (emailUsedByAnotherRegistration) {
            throw new IllegalStateException(
                    "This email is already registered for this event.");
        }

        if (event.getStatus() == EventStatus.CLOSED) {
            throw new IllegalStateException(
                    "This event is closed.");
        }

        if (LocalDateTime.now()
                .isAfter(event.getFinalAcceptanceDate())) {
            throw new IllegalStateException(
                    "Registration deadline has passed.");
        }

        int guestCount = dto.getGuests().size();
        int actualTotal = 1 + guestCount;

        if (dto.getTotalRegistration() != actualTotal) {
            throw new IllegalStateException(
                    "Total registration must be equal to the registrant plus the number of guests.");
        }

        Integer veganMenuCount =
                dto.getVeganMenuCount() == null
                        ? 0
                        : dto.getVeganMenuCount();

        if (veganMenuCount > dto.getTotalRegistration()) {
            throw new IllegalStateException(
                    "Vegan menu count cannot be greater than total registration.");
        }

        if (veganMenuCount > 0 &&
                (dto.getVeganAttendeeNames() == null ||
                        dto.getVeganAttendeeNames().isBlank())) {

            throw new IllegalStateException(
                    "Please enter the names of the attendees who will have the vegan menu.");
        }

        Long currentTotal =
                registrationRepository
                        .getTotalRegistrationByEventId(event.getId());

        long newEventTotal =
                currentTotal
                        - registration.getTotalRegistration()
                        + dto.getTotalRegistration();

        if (newEventTotal > event.getNumberOfAttendees()) {
            throw new IllegalStateException(
                    "There is not enough capacity for this registration.");
        }

        registration.setName(dto.getName());
        registration.setEmail(dto.getEmail());
        registration.setPhone(dto.getPhone());
        registration.setTotalRegistration(dto.getTotalRegistration());
        registration.setVeganMenuCount(veganMenuCount);
        registration.setVeganAttendeeNames(dto.getVeganAttendeeNames());
        registration.getGuests().clear();

        for (GuestDTO guestDTO : dto.getGuests()) {

            Guest guest = new Guest();

            guest.setName(guestDTO.getName());
            guest.setRegistration(registration);

            registration.getGuests().add(guest);
        }

        Registration savedRegistration =
                registrationRepository.save(registration);

        try {

            emailService.sendRegistrationUpdate(savedRegistration);

        } catch (Exception e) {

            System.out.println(
                    "Update email could not be sent: "
                            + e.getClass().getName()
                            + " - "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return getRegistrationByCode(registrationCode);
    }

    @Transactional
    public RegistrationResponseDTO cancelRegistration(
            String registrationCode) {

        Registration registration = registrationRepository
                .findByRegistrationCode(registrationCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Registration not found."));

        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            throw new IllegalStateException(
                    "This registration is already cancelled.");
        }

        eventRepository
                .findByEventCodeWithLock(
                        registration.getEvent().getEventCode()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found."));

        registration.setStatus(RegistrationStatus.CANCELLED);

        Registration savedRegistration =
                registrationRepository.save(registration);

        try {
            emailService.sendRegistrationCancellation(savedRegistration);
        } catch (Exception e) {
            System.out.println(
                    "Cancellation email could not be sent: " + e.getMessage()
            );
        }

        return getRegistrationByCode(registrationCode);
    }

    public List<RegistrationResponseDTO> getRegistrationsByEventId(
            Long eventId) {

        List<Registration> registrations =
                registrationRepository
                        .findByEventId(eventId);

        List<RegistrationResponseDTO> response =
                new ArrayList<>();

        for (Registration registration : registrations) {

            RegistrationResponseDTO dto =
                    new RegistrationResponseDTO();

            dto.setRegistrationCode(
                    registration.getRegistrationCode()
            );

            dto.setName(
                    registration.getName()
            );

            dto.setEmail(
                    registration.getEmail()
            );

            dto.setPhone(
                    registration.getPhone()
            );

            dto.setTotalRegistration(
                    registration.getTotalRegistration()
            );

            dto.setVeganMenuCount(
                    registration.getVeganMenuCount()
            );

            dto.setVeganAttendeeNames(
                    registration.getVeganAttendeeNames()
            );

            dto.setStatus(
                    registration.getStatus()
            );


            // Event information

            dto.setEventCode(
                    registration
                            .getEvent()
                            .getEventCode()
            );

            dto.setEventName(
                    registration
                            .getEvent()
                            .getEventName()
            );

            dto.setEventDate(
                    registration
                            .getEvent()
                            .getDate()
            );

            dto.setEventTime(
                    registration
                            .getEvent()
                            .getTime()
            );

            dto.setEventAddress(
                    registration
                            .getEvent()
                            .getAddress()
            );


            // Guest information

            for (Guest guest :
                    registration.getGuests()) {

                GuestDTO guestDTO =
                        new GuestDTO();

                guestDTO.setName(
                        guest.getName()
                );

                dto.getGuests().add(
                        guestDTO
                );
            }

            response.add(dto);
        }

        return response;
    }

    public EventParticipantSummaryDTO getEventParticipantSummary(
            Long eventId) {

        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found."));

        Long activeRegisteredPeople =
                registrationRepository
                        .getTotalRegistrationByEventId(eventId);

        long activeRegistrations =
                registrationRepository
                        .countByEventIdAndStatus(
                                eventId,
                                RegistrationStatus.ACTIVE
                        );

        long cancelledRegistrations =
                registrationRepository
                        .countByEventIdAndStatus(
                                eventId,
                                RegistrationStatus.CANCELLED
                        );

        int remainingCapacity =
                event.getNumberOfAttendees()
                        - activeRegisteredPeople.intValue();

        EventParticipantSummaryDTO summary =
                new EventParticipantSummaryDTO();

        summary.setCapacity(
                event.getNumberOfAttendees());

        summary.setActiveRegisteredPeople(
                activeRegisteredPeople);

        summary.setRemainingCapacity(
                remainingCapacity);

        summary.setActiveRegistrations(
                activeRegistrations);

        summary.setCancelledRegistrations(
                cancelledRegistrations);

        return summary;
    }

    //https://localhost:8080/my-registration/RG-A82KLP user will use this code for editing responses.
    private String generateRegistrationCode() {

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        String registrationCode;

        do {
            StringBuilder sb = new StringBuilder("RG-");
            for (int i = 0; i < 6; i++) {
                sb.append(
                        chars.charAt(
                                random.nextInt(chars.length())
                        )
                );
            }
            registrationCode = sb.toString();
        } while (
                registrationRepository
                        .existsByRegistrationCode(registrationCode)
        );
        return registrationCode;
    }

}