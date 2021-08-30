package com.example.springboot.service;

import com.example.springboot.config.CustomMessageSourceConfiguration;
import com.example.springboot.web.controller.dto.ColumnError;
import com.example.springboot.web.controller.dto.ErrorResponse;
import com.example.springboot.web.controller.dto.FriendRecord;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParserServiceImpl implements ParserService {

    @Autowired
    private CustomMessageSourceConfiguration messageSourceConfiguration;
    @Autowired
    private final ParseServiceHelper parseServiceHelper;

    @Override
    @SneakyThrows
    public List<FriendRecord> parse(MultipartFile file) {
        InputStream initialStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(initialStream);
        Sheet sheet = parseServiceHelper.getSheet(workbook);
        FriendRecord friendRecord ;
        CellReference cr = new CellReference("D5");
        String name = sheet.getRow(cr.getRow()).getCell(cr.getCol()).getStringCellValue();
        log.info("Total row: " + sheet.getLastRowNum());
        List<FriendRecord> friendRecordList = new ArrayList<>();

        for (Row row : sheet) {
            if (row.getRowNum() >= 13) {
                friendRecord = FriendRecord.builder().build();
                friendRecord.setRowNumber(row.getRowNum() + 1);
                friendRecord.setNickName(name);

                friendRecord.setFirstName(getStringCell(row, FileColumn.FIRST_NAME));
                friendRecord.setMiddleName(getStringCell(row, FileColumn.MIDDLE_NAME));
                friendRecord.setLastName(getStringCell(row, FileColumn.LAST_NAME));
                friendRecord.setMobileNumber(getStringCell(row, FileColumn.MOBILE_NUMBER));
                friendRecord.setEmailId(getStringCell(row, FileColumn.EMAIL_ID));
                friendRecord.setBirthDate(getDateCell(row, FileColumn.DATE_OF_BIRTH));
                friendRecord.setStreet(getStringCell(row, FileColumn.STREET));
                friendRecord.setHouseNumber(getNumericCell(row, FileColumn.HOUSE_NUMBER));
                friendRecord.setZipCode(getStringCell(row, FileColumn.ZIP_CODE));
                friendRecord.setCity(getStringCell(row, FileColumn.CITY));
                friendRecord.setField1(getStringCell(row, FileColumn.FIELD1));
                friendRecord.setField2(getStringCell(row, FileColumn.FIELD2));
                friendRecord.setField3(getStringCell(row, FileColumn.FIELD3));

                if (StringUtils.isNotBlank(friendRecord.getFirstName())  || StringUtils.isNotBlank(friendRecord.getLastName())) {
                    friendRecordList.add(friendRecord);
                }
            }
        }
        return friendRecordList;
    }

    @Override
    public List<ErrorResponse> isValid(List<FriendRecord> friendRecordList) {
        System.out.println(friendRecordList);
        Validator validator = messageSourceConfiguration.getValidator();
        List<ErrorResponse> errorResponseList = new ArrayList<>();
        ErrorResponse errorResponse;

        for (FriendRecord rec : friendRecordList) {
            errorResponse = new ErrorResponse();
            List<ColumnError> columnErrors = new ArrayList<>();

            Set<ConstraintViolation<FriendRecord>> violation = validator.validate(rec);

            errorResponse.setRowNumber(rec.getRowNumber());
            violation.forEach(v ->
                    columnErrors.add(
                            ColumnError.builder().
                                    columnName(v.getPropertyPath().toString())
                                    .error(v.getMessage()).build()));


            errorResponse.setColumnErrors(columnErrors);
            if (errorResponse.getColumnErrors().size() > 0) {
                errorResponseList.add(errorResponse);
            }
        }

        return errorResponseList;
    }

    private String getStringCell(Row row, FileColumn constant) {
        if(Objects.isNull(row.getCell(constant.columnNumber)) ){
            return null;
        }
        if (row.getCell(constant.columnNumber).getCellType() == CellType.NUMERIC) {
            row.getCell(constant.columnNumber).setCellType(CellType.STRING);
        }
        return row.getCell(constant.columnNumber).getStringCellValue();
    }

    private Long getLongCell(Row row, FileColumn constant) {
        if(Objects.isNull(row.getCell(constant.columnNumber)) ){
            return null;
        }
        return (long) row.getCell(constant.columnNumber).getNumericCellValue();
    }

    private Long getNumericCell(Row row, FileColumn constant) {
        if(Objects.isNull(row.getCell(constant.columnNumber)) ){
            return null;
        }
        if (row.getCell(constant.columnNumber).getCellType() == CellType.NUMERIC) {
            return (long) row.getCell(constant.columnNumber).getNumericCellValue();
        } else {
            if (StringUtils.isNotBlank(row.getCell(constant.columnNumber).getStringCellValue())) {
                return Long.parseLong(row.getCell(constant.columnNumber).getStringCellValue());
            }else{
                return null;//if cell type is String and value is empty
            }
        }
    }

    private LocalDate getDateCell(Row row, FileColumn constant) {
        if (Objects.isNull(row.getCell(constant.columnNumber))) {
            return null;
        }
        if (row.getCell(constant.columnNumber).getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(row.getCell(constant.columnNumber))) {
                return row.getCell(constant.columnNumber).getDateCellValue().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            }
        }
        return null;//It can not be parse
    }
}
