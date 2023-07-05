package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.payload.resource.PrescriptionExportResource;
import com.tech.exception.custom.DirectoryCreationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionPdfService {

    private final ApiMessages apiMessages;

    @Value("${export.folder.path.pdf}")
    private String pdfFolderPath;

    @Value("${export.file.path.background-img}")
    private String backgroundImgPath;

    public void createPdf(PrescriptionExportResource dataSource) {

        String pdfFilePath = prepareDirectoriesAndExcelFilePath(dataSource.getFileName());

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(465, 600));
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            File backgroundFile = new File(backgroundImgPath);
            BufferedImage backgroundImage = ImageIO.read(backgroundFile);
            PDImageXObject background = LosslessFactory.createFromImage(document, backgroundImage);
            contentStream.drawImage(background, 0, 0, 465, 600);

            contentStream.setNonStrokingColor(Color.BLUE);
            contentStream.setFont(PDType1Font.TIMES_BOLD, 16);

            String doctorFullName = "Dr." + dataSource.getDoctorFirstName() + " " + dataSource.getDoctorLastName();
            String patientFullName = dataSource.getPatientFirstName() + " " + dataSource.getPatientLastName();

            contentStream.beginText();
            contentStream.newLineAtOffset(300, 520);
            contentStream.showText(doctorFullName);

            contentStream.setNonStrokingColor(Color.DARK_GRAY);
            contentStream.setFont(PDType1Font.COURIER, 12);
            contentStream.newLineAtOffset(-230, -83);
            contentStream.showText(dataSource.getPatientSSN());
            contentStream.newLineAtOffset(+230, 0);
            contentStream.showText(patientFullName);
            contentStream.newLineAtOffset(-230, -24);
            contentStream.showText(dataSource.getPatientAge());
            contentStream.newLineAtOffset(+230, 0);
            contentStream.showText(dataSource.getPatientGender());
            contentStream.newLineAtOffset(-230, -24);
            contentStream.showText(dataSource.getDate());
            contentStream.newLineAtOffset(+230, 0);
            contentStream.showText(dataSource.getPatientPhoneNum());
            contentStream.newLineAtOffset(-230, -24);
            contentStream.showText(dataSource.getPatientAddress());
            contentStream.newLineAtOffset(+230, 0);
            contentStream.newLineAtOffset(-230, -24);

            int num = 1;
            for (String medicine : dataSource.getMedicines()) {
                contentStream.showText(num++ + "-] " + medicine);
                contentStream.newLineAtOffset(0, -24);
            }

            contentStream.endText();
            contentStream.close();

            document.save(pdfFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String prepareDirectoriesAndExcelFilePath(String fileName) {
        Path pdfPath = Paths.get(pdfFolderPath);
        createDirectories(pdfPath.toString());
        Path pdfFilePath = pdfPath.resolve(fileName + ".pdf");
        if (Files.exists(pdfFilePath)) {
            return pdfPath.resolve(fileName + "_[" + System.currentTimeMillis() + "].pdf").toString();
        }
        return pdfFilePath.toString();
    }

    private void createDirectories(String path) {
        Path directoryPath = Paths.get(path);

        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                log.error("Directory creation error: {}", e.getMessage());
                throw new DirectoryCreationException(
                        String.format(apiMessages.getMessage("error.creation.folder"), e.getMessage())
                );
            }
        }
    }


}
