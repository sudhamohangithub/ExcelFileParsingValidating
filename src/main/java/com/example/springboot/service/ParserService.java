package com.example.springboot.service;

import com.example.springboot.web.controller.dto.ErrorResponse;
import com.example.springboot.web.controller.dto.FriendRecord;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ParserService {
    List<FriendRecord> parse(MultipartFile file);
    List<ErrorResponse> isValid(List<FriendRecord> realEstateRecordList);
}
