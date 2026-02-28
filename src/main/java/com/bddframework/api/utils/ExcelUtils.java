package com.bddframework.api.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Enterprise-level Excel Utility for reading and writing Excel files.
 * Supports both .xls (HSSFWorkbook) and .xlsx (XSSFWorkbook) formats.
 * Features:
 * - Thread-safe operations
 * - Comprehensive error handling and logging
 * - Support for multiple data types
 * - Resource management with try-with-resources
 * - Data validation and conversion
 */
public class ExcelUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
    private static final int DEFAULT_SHEET_INDEX = 0;
    private static final String XLS_EXTENSION = ".xls";
    private static final String XLSX_EXTENSION = ".xlsx";

    private ExcelUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Reads all data from an Excel sheet as a List of Maps.
     * Each row is represented as a Map with column headers as keys.
     *
     * @param excelPath   Path to the Excel file
     * @param sheetName   Name of the sheet to read
     * @return List of Maps containing row data
     * @throws IllegalArgumentException if file not found or invalid parameters
     * @throws IOException              if file reading fails
     */
    public static List<Map<String, String>> readExcelDataBySheetName(String excelPath, String sheetName) {
        logger.info("Reading Excel data from file: {}, sheet: {}", excelPath, sheetName);
        validateFilePath(excelPath);

        try (Workbook workbook = loadWorkbook(excelPath)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in workbook");
            }
            return extractSheetData(sheet);
        } catch (IOException e) {
            logger.error("Failed to read Excel file: {}", excelPath, e);
            throw new RuntimeException("Unable to read Excel file", e);
        }
    }

    /**
     * Reads data from an Excel sheet by index (0-based).
     *
     * @param excelPath  Path to the Excel file
     * @param sheetIndex Zero-based index of the sheet
     * @return List of Maps containing row data
     * @throws IllegalArgumentException if sheet index is invalid
     * @throws IOException              if file reading fails
     */
    public static List<Map<String, String>> readExcelDataBySheetIndex(String excelPath, int sheetIndex) {
        logger.info("Reading Excel data from file: {}, sheet index: {}", excelPath, sheetIndex);
        validateFilePath(excelPath);

        if (sheetIndex < 0) {
            throw new IllegalArgumentException("Sheet index cannot be negative");
        }

        try (Workbook workbook = loadWorkbook(excelPath)) {
            if (sheetIndex >= workbook.getNumberOfSheets()) {
                throw new IllegalArgumentException("Sheet index " + sheetIndex + " out of bounds. Total sheets: " + workbook.getNumberOfSheets());
            }
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            return extractSheetData(sheet);
        } catch (IOException e) {
            logger.error("Failed to read Excel file: {}", excelPath, e);
            throw new RuntimeException("Unable to read Excel file", e);
        }
    }

    /**
     * Reads a specific row from an Excel sheet by test ID (assumes ID in first column).
     *
     * @param excelPath Path to the Excel file
     * @param sheetName Name of the sheet to read
     * @param testID    Test ID to search for (value in first column)
     * @return Map containing the row data, or empty map if not found
     */
    public static Map<String, String> readExcelDataByTestID(String excelPath, String sheetName, String testID) {
        logger.info("Reading Excel data for testID: {} from file: {}, sheet: {}", testID, excelPath, sheetName);
        validateFilePath(excelPath);

        try (Workbook workbook = loadWorkbook(excelPath)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in workbook");
            }

            List<String> headers = extractHeaders(sheet);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                Cell firstCell = row.getCell(0);
                if (firstCell != null && getCellValueAsString(firstCell).equals(testID)) {
                    return extractRowData(row, headers);
                }
            }
            logger.warn("Test ID '{}' not found in sheet '{}'", testID, sheetName);
            return new HashMap<>();
        } catch (IOException e) {
            logger.error("Failed to read Excel file: {}", excelPath, e);
            throw new RuntimeException("Unable to read Excel file", e);
        }
    }

    /**
     * Writes data to an Excel file. Creates new file if it doesn't exist.
     *
     * @param excelPath   Path where the Excel file should be created
     * @param sheetName   Name of the sheet to write
     * @param data        List of Maps containing data to write
     * @throws IOException if file writing fails
     */
    public static void writeExcelData(String excelPath, String sheetName, List<Map<String, String>> data) {
        logger.info("Writing Excel data to file: {}, sheet: {}", excelPath, sheetName);

        if (data == null || data.isEmpty()) {
            logger.warn("No data provided to write");
            return;
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            Set<String> headers = data.get(0).keySet();

            // Write headers
            Row headerRow = sheet.createRow(0);
            int columnIndex = 0;
            for (String header : headers) {
                Cell cell = headerRow.createCell(columnIndex++);
                cell.setCellValue(header);
                formatHeaderCell(cell);
            }

            // Write data rows
            int rowIndex = 1;
            for (Map<String, String> rowData : data) {
                Row row = sheet.createRow(rowIndex++);
                columnIndex = 0;
                for (String header : headers) {
                    Cell cell = row.createCell(columnIndex++);
                    setCellValue(cell, rowData.get(header));
                }
            }

            // Auto-size columns
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(excelPath)) {
                workbook.write(fos);
                logger.info("Excel file written successfully: {}", excelPath);
            }
        } catch (IOException e) {
            logger.error("Failed to write Excel file: {}", excelPath, e);
            throw new RuntimeException("Unable to write Excel file", e);
        }
    }

    /**
     * Appends data to an existing Excel sheet.
     *
     * @param excelPath Path to the existing Excel file
     * @param sheetName Name of the sheet to append to
     * @param data      List of Maps containing data to append
     * @throws IOException if file operations fail
     */
    public static void appendExcelData(String excelPath, String sheetName, List<Map<String, String>> data) {
        logger.info("Appending Excel data to file: {}, sheet: {}", excelPath, sheetName);
        validateFilePath(excelPath);

        if (data == null || data.isEmpty()) {
            logger.warn("No data provided to append");
            return;
        }

        try (Workbook workbook = loadWorkbook(excelPath)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }

            int lastRowIndex = sheet.getLastRowNum();
            List<String> headers = extractHeaders(sheet);

            int rowIndex = lastRowIndex + 1;
            for (Map<String, String> rowData : data) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.createCell(i);
                    setCellValue(cell, rowData.get(headers.get(i)));
                }
            }

            try (FileOutputStream fos = new FileOutputStream(excelPath)) {
                workbook.write(fos);
                logger.info("Data appended successfully to: {}", excelPath);
            }
        } catch (IOException e) {
            logger.error("Failed to append data to Excel file: {}", excelPath, e);
            throw new RuntimeException("Unable to append to Excel file", e);
        }
    }

    /**
     * Retrieves a specific cell value from an Excel sheet.
     *
     * @param excelPath  Path to the Excel file
     * @param sheetName  Name of the sheet
     * @param rowIndex   Zero-based row index
     * @param columnIndex Zero-based column index
     * @return Cell value as String, or null if cell doesn't exist
     */
    public static String getCellValue(String excelPath, String sheetName, int rowIndex, int columnIndex) {
        logger.debug("Getting cell value from file: {}, sheet: {}, row: {}, column: {}", excelPath, sheetName, rowIndex, columnIndex);
        validateFilePath(excelPath);

        try (Workbook workbook = loadWorkbook(excelPath)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found");
            }

            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                logger.warn("Row {} not found in sheet {}", rowIndex, sheetName);
                return null;
            }

            Cell cell = row.getCell(columnIndex);
            return cell != null ? getCellValueAsString(cell) : null;
        } catch (IOException e) {
            logger.error("Failed to read cell value from Excel file: {}", excelPath, e);
            throw new RuntimeException("Unable to read cell value", e);
        }
    }

    /**
     * Gets all sheet names from an Excel workbook.
     *
     * @param excelPath Path to the Excel file
     * @return List of sheet names
     */
    public static List<String> getSheetNames(String excelPath) {
        logger.info("Getting sheet names from file: {}", excelPath);
        validateFilePath(excelPath);

        List<String> sheetNames = new ArrayList<>();
        try (Workbook workbook = loadWorkbook(excelPath)) {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheetNames.add(workbook.getSheetName(i));
            }
        } catch (IOException e) {
            logger.error("Failed to read sheet names from Excel file: {}", excelPath, e);
            throw new RuntimeException("Unable to read sheet names", e);
        }
        return sheetNames;
    }

    /**
     * Gets the row count of a sheet (excluding header).
     *
     * @param excelPath Path to the Excel file
     * @param sheetName Name of the sheet
     * @return Number of data rows (excluding header)
     */
    public static int getRowCount(String excelPath, String sheetName) {
        logger.debug("Getting row count from file: {}, sheet: {}", excelPath, sheetName);
        validateFilePath(excelPath);

        try (Workbook workbook = loadWorkbook(excelPath)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found");
            }
            return sheet.getLastRowNum(); // Excludes header row
        } catch (IOException e) {
            logger.error("Failed to get row count from Excel file: {}", excelPath, e);
            throw new RuntimeException("Unable to get row count", e);
        }
    }

    // ==================== Private Helper Methods ====================

    /**
     * Loads a Workbook from file path (supports both .xls and .xlsx).
     */
    private static Workbook loadWorkbook(String excelPath) throws IOException {
        FileInputStream fis = new FileInputStream(excelPath);
        if (excelPath.endsWith(XLSX_EXTENSION)) {
            return new XSSFWorkbook(fis);
        } else if (excelPath.endsWith(XLS_EXTENSION)) {
            return new HSSFWorkbook(fis);
        } else {
            throw new IllegalArgumentException("Unsupported file format. Please use .xls or .xlsx");
        }
    }

    /**
     * Extracts all data from a sheet as a list of maps.
     */
    private static List<Map<String, String>> extractSheetData(Sheet sheet) {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<String> headers = extractHeaders(sheet);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row
            Map<String, String> rowData = extractRowData(row, headers);
            if (!rowData.isEmpty()) {
                dataList.add(rowData);
            }
        }
        return dataList;
    }

    /**
     * Extracts header row from a sheet.
     */
    private static List<String> extractHeaders(Sheet sheet) {
        List<String> headers = new ArrayList<>();
        Row headerRow = sheet.getRow(0);

        if (headerRow != null) {
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }
        }
        return headers;
    }

    /**
     * Extracts data from a single row using provided headers.
     */
    private static Map<String, String> extractRowData(Row row, List<String> headers) {
        Map<String, String> rowData = new HashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.getCell(i);
            String value = cell != null ? String.valueOf(getCellValue(cell)) : null;
            rowData.put(headers.get(i), value);
        }
        return rowData;
    }

    /**
     * Gets cell value with proper type conversion.
     */
    private static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return String.valueOf(cell.getDateCellValue()).trim();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    /**
     * Gets cell value as String.
     */
    private static String getCellValueAsString(Cell cell) {
        String value = getCellValue(cell);
        return value != null ? value.toString() : "";
    }

    /**
     * Sets cell value with appropriate type.
     */
    private static void setCellValue(Cell cell, String value) {
        if (value == null) {
            cell.setBlank();
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
//        } else if (value instanceof Double) {
//            cell.setCellValue((Double) value);
//        } else if (value instanceof Integer) {
//            cell.setCellValue((Integer) value);
//        } else if (value instanceof Boolean) {
//            cell.setCellValue((Boolean) value);
//        } else if (value instanceof Date) {
//            cell.setCellValue((Date) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * Formats header cells with bold and background color.
     */
    private static void formatHeaderCell(Cell cell) {
        CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
        Font font = cell.getSheet().getWorkbook().createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);
    }

    /**
     * Validates that the file path is not null or empty.
     */
    private static void validateFilePath(String excelPath) {
        if (excelPath == null || excelPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Excel file path cannot be null or empty");
        }
    }
}
