package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.exception.custom.ConflictException;
import com.tech.exception.custom.DataExportException;
import com.tech.exception.custom.DirectoryCreationException;
import com.tech.exception.custom.MissingArgumentException;
import com.tech.payload.annotations.custom.ExportToExcel;
import com.tech.payload.response.simple.SimpleAppointmentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelWriteService {
    private ApiMessages apiMessages;

    public void writeAppointmentsToExcel(List<SimpleAppointmentResponse> appointments, String fileName, String sheetName) {

        Path tempPath = Paths.get(System.getProperty("user.home"), "desktop", ".emergencyService", "appointments", "temp");
        createDirectories(tempPath.toString());
        String excelPath = tempPath.resolve(fileName + ".xlsx").toString();

        try (
                FileOutputStream fos = new FileOutputStream(excelPath);
                Workbook workbook = new XSSFWorkbook();
        ) {


            Sheet sheet = workbook.createSheet(sheetName);
            CellStyle headerStyle = buildHeaderStyle(workbook);
            CellStyle dataCellStyle = buildDataCellStyle(workbook);

            Row header = sheet.createRow(0);
            int cellIndex = 0;
            List<Field> fields = getIndexedAnnotatedFields(SimpleAppointmentResponse.class, ExportToExcel.class);
            for (Field field : fields) {
                ExportToExcel annotation = field.getAnnotation(ExportToExcel.class);
                String columnName = annotation.text();
                int columnWidth = annotation.width();

                sheet.setColumnWidth(cellIndex, columnWidth);

                Cell headerCell = header.createCell(cellIndex++);
                headerCell.setCellValue(columnName);
                headerCell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;
            for (SimpleAppointmentResponse appointment : appointments) {
                Row dataRow = sheet.createRow(rowIndex++);
                cellIndex = 0;
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(appointment);

                    Cell dataCell = dataRow.createCell(cellIndex++);
                    dataCell.setCellValue(String.valueOf(value));
                    dataCell.setCellStyle(dataCellStyle);
                }
            }

            workbook.write(fos);
        } catch (IOException e) {
            log.error("Excel data export error : {}", e.getMessage());
            throw new DataExportException(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error("Excel data export error : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private CellStyle buildHeaderStyle(Workbook workbook) {

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);

        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        return headerStyle;
    }

    private CellStyle buildDataCellStyle(Workbook workbook) {

        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setBorderTop(BorderStyle.THIN);
        dataCellStyle.setBorderBottom(BorderStyle.THIN);
        dataCellStyle.setBorderLeft(BorderStyle.THIN);
        dataCellStyle.setBorderRight(BorderStyle.THIN);

        return dataCellStyle;
    }


    private void createDirectories(String path) {
        Path directoryPath = Paths.get(path);

        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                log.error("Directory creation error: {}", e.getMessage());
                throw new DirectoryCreationException(String.format(apiMessages.getMessage("error.creation.folder"), e.getMessage()));
            }
        }
    }

    private List<Field> getIndexedAnnotatedFields(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<Field> annotatedFields = new ArrayList<>();
        Set<Integer> indexSet = new HashSet<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                int index = field.getAnnotation(ExportToExcel.class).index();
                if (indexSet.contains(index)) {
                    throw new ConflictException("ExportToExcel.class duplicate index value: " + index);
                } else if (index == -1) {
                    throw new MissingArgumentException("ExportToExcel.class index value missing: " + index);
                }
                annotatedFields.add(field);
                indexSet.add(index);
            }
        }

        annotatedFields.sort(Comparator.comparingInt(field -> field.getAnnotation(ExportToExcel.class).index()));
        return annotatedFields;
    }

}
