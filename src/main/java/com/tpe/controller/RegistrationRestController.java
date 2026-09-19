package com.tpe.controller;

import com.tpe.domain.Registration;
import com.tpe.dto.RegistrationCreateDTO;
import com.tpe.dto.RegistrationEventDTO;
import com.tpe.service.EventService;
import com.tpe.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import com.tpe.dto.RegistrationResponseDTO;
import com.tpe.dto.RegistrationUpdateDTO;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationRestController {

    private final EventService eventService;
    private final RegistrationService registrationService;

    @GetMapping("/event/{eventCode}") //http://localhost:8080/api/registartion/event/eventcode
    public RegistrationEventDTO getEventForRegistration(
            @PathVariable String eventCode){

        return eventService.getRegistrationEvent(eventCode);

    }

    @PostMapping("/event/{eventCode}")
    public ResponseEntity<Map<String, Object>> createRegistration(
            @PathVariable String eventCode,
            @Valid @RequestBody RegistrationCreateDTO dto){

        System.out.println("========== CONTROLLER ==========");
        Registration registration =
                registrationService.createRegistration(eventCode, dto);

        Map<String, Object> response = new HashMap<>();

        response.put("message",
                "Registration has been successfully completed.");

        response.put("registrationCode",
                registration.getRegistrationCode());

        response.put("editUrl",
                "http://localhost:8080/my-registration/"
                        + registration.getRegistrationCode());

        return ResponseEntity.ok(response);

    }

    @GetMapping("/{registrationCode}") //http://localhost:8080/api/registartion/registrationCode
    public RegistrationResponseDTO getRegistration(
            @PathVariable String registrationCode) {

        return registrationService
                .getRegistrationByCode(registrationCode);
    }

    @PutMapping("/{registrationCode}")  //http://localhost:8080/api/registartion/registrationCode
    public RegistrationResponseDTO updateRegistration(
            @PathVariable String registrationCode,
            @Valid @RequestBody RegistrationUpdateDTO dto) {

        return registrationService
                .updateRegistration(registrationCode, dto);
    }

    @PutMapping("/{registrationCode}/cancel") //http://localhost:8080/api/registartion/registrationCode/cancel
    public RegistrationResponseDTO cancelRegistration(
            @PathVariable String registrationCode) {

        return registrationService
                .cancelRegistration(registrationCode);
    }


}