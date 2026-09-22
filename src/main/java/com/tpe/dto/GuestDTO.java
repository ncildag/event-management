package com.tpe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestDTO {

    @NotBlank(message = "Guest name is required.")
    private String name;

    // True if the guest is a child under 10 years old.
    private Boolean child = false;

    // True if the child is under 3 years old.
    // If this is true, child must also be true.
    private Boolean underThree = false;
}