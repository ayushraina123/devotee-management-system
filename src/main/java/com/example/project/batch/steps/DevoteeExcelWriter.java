package com.example.project.batch.steps;

import com.example.project.dtos.DevoteeExcelRowDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@StepScope
public class DevoteeExcelWriter implements ItemStreamWriter<DevoteeExcelRowDto> {

    @Value("#{jobParameters['donationType']}")
    private String donationType;

    private Workbook workbook;
    private Sheet sheet;
    private int rowNum;
    private Path outputPath;

    @Override
    public void open(ExecutionContext executionContext) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Devotees");
        rowNum = 0;

        String[] columns = {"First Name", "Last Name", "Father Name", "City", "State", "Country", "Pincode", "Donations", "Total Donated Amount"};
        Row header = sheet.createRow(rowNum++);
        for (int i = 0; i < columns.length; i++) {
            header.createCell(i).setCellValue(columns[i]);
        }

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        outputPath = Path.of(System.getProperty("user.home"), "Downloads", "Devotees_Data_" + donationType.toUpperCase() + "_" + dateTime + ".xlsx");
        try {
            Files.createDirectories(outputPath.getParent());
        } catch (Exception ignore) {
        }
    }

    @Override
    public void write(Chunk<? extends DevoteeExcelRowDto> chunk) {
        for (DevoteeExcelRowDto r : chunk) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(r.getFirstName());
            row.createCell(1).setCellValue(r.getLastName());
            row.createCell(2).setCellValue(r.getFatherName());
            row.createCell(3).setCellValue(r.getCity());
            row.createCell(4).setCellValue(r.getState());
            row.createCell(5).setCellValue(r.getCountry());
            row.createCell(6).setCellValue(r.getPincode());
            row.createCell(7).setCellValue(r.getDonations());
            row.createCell(8).setCellValue(r.getTotalDonation() != null ? r.getTotalDonation().toPlainString() : "0");
        }
    }

    @Override
    public void update(ExecutionContext executionContext) {
    }

    @Override
    public void close() {
        for (int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }
        try (FileOutputStream fos = new FileOutputStream(outputPath.toFile())) {
            workbook.write(fos);
        } catch (Exception e) {
            throw new RuntimeException("Failed writing Excel to " + outputPath, e);
        }
        try {
            workbook.close();
        } catch (Exception ignore) {
        }
    }
}
