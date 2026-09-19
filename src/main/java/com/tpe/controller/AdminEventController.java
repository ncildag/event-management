package com.tpe.controller;

import com.tpe.domain.Event;
import com.tpe.dto.EventCreateDTO;
import com.tpe.dto.EventUpdateDTO;
import com.tpe.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.tpe.dto.RegistrationResponseDTO;
import com.tpe.service.RegistrationService;
import com.tpe.dto.EventParticipantSummaryDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;
    private final RegistrationService registrationService;

    //<-------------------------------------->
    //Bring create event form
    @GetMapping("/create") //http://localhost:8080/admin/events/create
    public String showCreateEventForm(Model model) {

        model.addAttribute("event", new EventCreateDTO());

        return "admin/create-event";
    }

    //<-----------------EVENT CREATION--------------------->
    //Create event via form
    @PostMapping("/create")
    public String createEvent(@ModelAttribute("event") EventCreateDTO dto) {

        eventService.createEvent(dto);

        return "redirect:/admin/events";
    }

    //Create event via api
    @PostMapping("/api/create")  //http://localhost:8080/admin/events/api/create/
    @ResponseBody
    public Map<String, Object> createEventViaApi(
            @RequestBody EventCreateDTO dto) {

        Event event = eventService.createEvent(dto);

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Event has been successfully created.");
        response.put("event", event);

        return response;
    }

    //<-------------------------------------->

    //After create event redirect admin/events return page in html.
    @GetMapping
    public String getAllEvents(Model model) {

        model.addAttribute("events", eventService.getAllEvents());

        return "admin/events";
    }

    //Return getAll events with json for apis
    @GetMapping("/api") //http://localhost:8080/admin/events/api
    @ResponseBody
    public List<Event> getAllEventsAsJson() {
        return eventService.getAllEvents();
    }

    //<-------------------------------------->

    //Open the event to see details.
    @GetMapping("/{id}")
    public String getEventById(
            @PathVariable Long id,
            Model model) {

        Event event =
                eventService.getEventById(id);

        List<RegistrationResponseDTO> registrations =
                registrationService
                        .getRegistrationsByEventId(id);

        EventParticipantSummaryDTO summary =
                registrationService
                        .getEventParticipantSummary(id);

        model.addAttribute(
                "event",
                event
        );

        model.addAttribute(
                "registrations",
                registrations
        );

        model.addAttribute(
                "summary",
                summary
        );

        return "admin/event-details";
    }

    //Return evet details with json for apis
    @GetMapping("/api/{id}") //http://localhost:8080/admin/events/api/+id
    @ResponseBody
    public Event getEventByIdAsJson(@PathVariable Long id){
        return eventService.getEventById(id);
    }

    //<------------------UPDATE EVENTS-------------------->

    //Update event via form - http://localhost:8080/admin/events/1/edit
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {

        Event event = eventService.getEventById(id);

        model.addAttribute("event", event);

        return "admin/edit-event";
    }

    //After clicking on update button redirect to admin/events page.
    @PostMapping("/{id}/edit")
    public String updateEvent(
            @PathVariable Long id,
            @ModelAttribute("event") EventUpdateDTO dto) {

        eventService.updateEvent(id, dto);

        return "redirect:/admin/events/" + id;
    }

    //Update via api
    @PutMapping("/api/update/{id}")  //http://localhost:8080/admin/events/api/update/id
    @ResponseBody
    public Map<String, Object> updateEventViaApi(
            @PathVariable Long id,
            @RequestBody EventUpdateDTO dto) {

        Event updatedEvent = eventService.updateEvent(id, dto);

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Event has been successfully updated.");
        response.put("event", updatedEvent);

        return response;
    }

    //<-------------------------------------->


    //------------DELETE EVENT---------------

    //Delete event via form
    @PostMapping("/{id}/delete") //http://localhost:8080/admin/events/{id}/delete
    public String deleteEvent(@PathVariable Long id) {

        eventService.deleteEvent(id);

        return "redirect:/admin/events";
    }

    //Delete event via api
    @DeleteMapping("/api/{id}") //http://localhost:8080/admin/events/api/{id}/
    @ResponseBody
    public Map<String, Object> deleteEventViaApi(@PathVariable Long id) {

        eventService.deleteEvent(id);

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Event has been successfully deleted.");
        response.put("eventId", id);

        return response;
    }

    //<-------------------------------------->


    //------------CLOSE and Reopen EVENT---------------
    @PostMapping("/{id}/close")
    public String closeEvent(@PathVariable Long id){

        eventService.closeEvent(id);

        return "redirect:/admin/events/" + id;
    }

    @PostMapping("/{id}/reopen")
    public String reopenEvent(@PathVariable Long id){

        eventService.reopenEvent(id);

        return "redirect:/admin/events/" + id;
    }

    //------------EXPORT PARTICIPANTS TO CSV---------------

    @GetMapping("/{id}/export-csv")
    public ResponseEntity<byte[]> exportParticipantsToCsv(
            @PathVariable Long id) {

        Event event = eventService.getEventById(id);

        List<RegistrationResponseDTO> registrations =
                registrationService.getRegistrationsByEventId(id);


        StringBuilder csv = new StringBuilder();


        // Excel UTF-8 BOM
        csv.append("\uFEFF");


        // CSV HEADER
        csv.append(
                "Name,Email,Phone,Total,Vegan Menus,Vegan Menu Owners,Status,Registration Code,Guests\n"
        );


        // CSV DATA
        for (RegistrationResponseDTO registration : registrations) {

            String guestNames = "";

            if (registration.getGuests() != null) {

                guestNames = registration
                        .getGuests()
                        .stream()
                        .map(guest -> guest.getName())
                        .reduce(
                                (guest1, guest2) ->
                                        guest1 + ", " + guest2
                        )
                        .orElse("");
            }


            csv.append(csvValue(registration.getName()))
                    .append(",")

                    .append(csvValue(registration.getEmail()))
                    .append(",")

                    .append(csvValue(registration.getPhone()))
                    .append(",")

                    .append(registration.getTotalRegistration())
                    .append(",")

                    .append(
                            registration.getVeganMenuCount() == null
                                    ? 0
                                    : registration.getVeganMenuCount()
                    )
                    .append(",")

                    .append(
                            csvValue(
                                    registration.getVeganAttendeeNames()
                            )
                    )
                    .append(",")

                    .append(csvValue(
                            String.valueOf(registration.getStatus())
                    ))
                    .append(",")

                    .append(csvValue(
                            registration.getRegistrationCode()
                    ))
                    .append(",")

                    .append(csvValue(guestNames))

                    .append("\n");
        }


        String fileName =
                "event-"
                        + event.getId()
                        + "-participants.csv";


        byte[] csvBytes =
                csv.toString()
                        .getBytes(StandardCharsets.UTF_8);


        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\""
                )
                .contentType(
                        new MediaType(
                                "text",
                                "csv",
                                StandardCharsets.UTF_8
                        )
                )
                .body(csvBytes);
    }


//------------CSV HELPER METHOD---------------

    private String csvValue(Object value) {

        if (value == null) {

            return "\"\"";
        }


        String text =
                value.toString()
                        .replace("\"", "\"\"");


        return "\"" + text + "\"";
    }


}