package com.tech.service;

import com.tech.service.abstracts.AbstractExcelExporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelWriteService extends AbstractExcelExporter {

    @Value("${export.folder.path.xls}")
    private String excelFolderPath;

    @Override
    protected String prepareDirectoriesAndExcelFilePath(String fileName) {
        Path excelPath = Paths.get(excelFolderPath);
        createDirectories(excelPath.toString());
        String excelFilePath = excelPath.resolve(fileName + ".xlsx").toString();
        return excelFilePath;
    }


    @Override
    protected CellStyle buildHeaderStyle(Workbook workbook) {

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);

        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THICK);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setTopBorderColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setLeftBorderColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setLeftBorderColor(IndexedColors.ROYAL_BLUE.getIndex());

        return headerStyle;
    }

    @Override
    protected CellStyle buildDataCellStyle(Workbook workbook) {

        CellStyle dataCellStyle = workbook.createCellStyle();

        dataCellStyle.setBorderTop(BorderStyle.THIN);
        dataCellStyle.setBorderBottom(BorderStyle.THIN);
        dataCellStyle.setBorderLeft(BorderStyle.THIN);
        dataCellStyle.setBorderRight(BorderStyle.THIN);

        return dataCellStyle;
    }


}
