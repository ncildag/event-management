package com.tpe.controller;

import com.tpe.domain.Event;
import com.tpe.dto.RegistrationCreateDTO;
import com.tpe.dto.RegistrationEventDTO;
import com.tpe.dto.RegistrationResponseDTO;
import com.tpe.service.EventService;
import com.tpe.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.tpe.domain.enums.EventStatus;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final EventService eventService;

    private final RegistrationService registrationService;


    // =====================================================
    // PUBLIC REGISTRATION PAGE
    // =====================================================

    @GetMapping("/register/{eventCode}")     //http://localhost:8080/register/ABCD1234
    public String showRegistrationPage(
            @PathVariable String eventCode,
            Model model) {

        Event event = eventService.getEventByCode(eventCode);
        RegistrationEventDTO registrationEvent = eventService.getRegistrationEvent(eventCode);


        // =========================================
        // CHECK IF REGISTRATION IS OPEN
        // =========================================

        boolean registrationOpen = true;
        String closedReason = null;


        // 1. Admin manually closed the event
        if (event.getStatus() == EventStatus.CLOSED) {

            registrationOpen = false;

            closedReason =
                    "This registration form is closed.";
        }


        // 2. Registration deadline has passed
        else if (LocalDateTime.now()
                .isAfter(event.getFinalAcceptanceDate())) {

            registrationOpen = false;

            closedReason =
                    "The registration deadline has passed.";
        }


        // 3. Event is fully booked
        else if (registrationEvent.getRemainingCapacity() <= 0) {

            registrationOpen = false;

            closedReason =
                    "This event is fully booked.";
        }


        // =========================================
        // SEND DATA TO HTML
        // =========================================

        model.addAttribute(
                "event",
                event
        );

        model.addAttribute(
                "remainingCapacity",
                registrationEvent.getRemainingCapacity()
        );

        model.addAttribute(
                "registrationOpen",
                registrationOpen
        );

        model.addAttribute(
                "closedReason",
                closedReason
        );

        model.addAttribute(
                "registration",
                new RegistrationCreateDTO()
        );


        return "registration-form";
    }


    // =====================================================
    // FIND MY REGISTRATION PAGE
    // =====================================================

    @GetMapping("/my-registration")
    public String showFindMyRegistrationPage() {

        return "find-my-registration";
    }


    // =====================================================
    // FIND REGISTRATION
    // =====================================================

    @GetMapping("/my-registration/find")
    public String findMyRegistration(
            @RequestParam String registrationCode) {

        return "redirect:/my-registration/"
                + registrationCode.trim();
    }


    // =====================================================
    // MY REGISTRATION PAGE
    // =====================================================

    @GetMapping("/my-registration/{registrationCode}")
    public String showMyRegistrationPage(
            @PathVariable String registrationCode,
            Model model) {

        RegistrationResponseDTO registration =
                registrationService
                        .getRegistrationByCode(registrationCode);

        RegistrationEventDTO registrationEvent =
                eventService
                        .getRegistrationEvent(
                                registration.getEventCode()
                        );

        model.addAttribute(
                "registration",
                registration
        );

        model.addAttribute(
                "remainingCapacity",
                registrationEvent.getRemainingCapacity()
        );

        return "my-registration";
    }

}