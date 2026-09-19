package com.tpe.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RegistrationCreateDTO {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    private String phone;

    @NotNull(message = "Total registration is required.")
    @Min(value = 1, message = "Total registration must be at least 1.")
    private Integer totalRegistration;

    @Min(value = 0, message = "Vegan menu count cannot be negative.")
    private Integer veganMenuCount = 0;

    private String veganAttendeeNames;

    @Valid
    @NotNull(message = "Guests list cannot be null.")
    private List<GuestDTO> guests = new ArrayList<>();
}