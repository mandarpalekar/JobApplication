package com.springsynergy.jobapp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DtoToStringConverter {

    private final ObjectMapper objectMapper;

    public <T> ResponseEntity<String> convertDtoToString(T dto) {
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
        }

        try {
            String dtoJson = objectMapper.writeValueAsString(dto);
            return ResponseEntity.ok(dtoJson);
        } catch (JsonProcessingException e) {
            String errorMessage = String.format("Error converting %s to JSON", dto.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
}
