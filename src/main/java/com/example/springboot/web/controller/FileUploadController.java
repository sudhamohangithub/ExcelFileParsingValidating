package com.example.springboot.web.controller;

import com.example.springboot.service.ParserService;
import com.example.springboot.web.controller.dto.ErrorResponse;
import com.example.springboot.web.controller.dto.FriendRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class FileUploadController {
	private final ParserService parserService;
	@PostMapping("/file-upload")
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
		log.debug("File Name is : "+file.getOriginalFilename());
		List<FriendRecord> friendRecordList = parserService.parse(file);
		List<ErrorResponse> errorResponseList = parserService.isValid(friendRecordList);
		if(errorResponseList.size()>0){
			return new ResponseEntity<>(errorResponseList, HttpStatus.BAD_REQUEST);
		}else {
			// Do Something
			log.debug("File upload called : "+file.getOriginalFilename());
			return new ResponseEntity<>(friendRecordList, HttpStatus.CREATED );
		}
	}
}
