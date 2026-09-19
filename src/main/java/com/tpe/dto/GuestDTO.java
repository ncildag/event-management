package com.tpe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestDTO {

    @NotBlank(message = "Guest name is required.")
    private String name;
}