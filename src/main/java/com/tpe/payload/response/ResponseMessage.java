package com.tpe.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

//Wrapper (sarmalayici sinif)

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

@JsonInclude(JsonInclude.Include.NON_NULL) //null olanlari frontend'e gonderme
public class ResponseMessage<E> { //Generic Type. Icinde User, Message, Product her sey olabilir

    private E object; //object yerine data veya payload isimleri de kullanilir

    private String message;

    private HttpStatus httpStatus;

}