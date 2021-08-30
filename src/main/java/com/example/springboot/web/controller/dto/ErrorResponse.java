package com.example.springboot.web.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private int rowNumber;
    private List<ColumnError> columnErrors;
}