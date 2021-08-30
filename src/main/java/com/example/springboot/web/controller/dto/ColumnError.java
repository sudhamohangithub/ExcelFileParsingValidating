package com.example.springboot.web.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnError {
    String columnName;
    String error;
}
