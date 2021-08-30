package com.example.springboot.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ParseServiceHelper {

    public Sheet getSheet(Workbook workbook) {
        Sheet sheet = workbook.getSheet("English");
        CellReference cr = new CellReference("D5");
        String name = sheet.getRow(cr.getRow()).getCell(cr.getCol()).getStringCellValue();

        List<Row> nonEmptyRowList = getNonEmptyRow(sheet);
        if(StringUtils.isNotEmpty(name ) && nonEmptyRowList.size() > 0 ){
            log.info("Using English sheet");
            return workbook.getSheet("English");
        }
        log.info("Using Dutch sheet");
        return workbook.getSheet("Dutch");
    }

    private List<Row> getNonEmptyRow(Sheet sheet) {
        List<Row> rowList = new ArrayList<>();
        for (Row row: sheet) {
            if (row.getRowNum() <= 13) continue;
            if(!checkEmptyRows(row)){
                rowList.add(row);
            }
        }
        return rowList;
    }

    private boolean checkEmptyRows(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && !cell.toString().isEmpty()) { return false; }
        }
        return true;
    }
}
